package com.ilevitsky.testproject.tasksystem.service;

import com.ilevitsky.testproject.tasksystem.dto.CommentDto;
import com.ilevitsky.testproject.tasksystem.dto.TaskDto;
import com.ilevitsky.testproject.tasksystem.dto.UserDto;
import com.ilevitsky.testproject.tasksystem.entity.Task;
import com.ilevitsky.testproject.tasksystem.entity.TaskPriority;
import com.ilevitsky.testproject.tasksystem.entity.TaskStatus;
import com.ilevitsky.testproject.tasksystem.entity.auth.User;
import com.ilevitsky.testproject.tasksystem.exception.OperationDeniedException;
import com.ilevitsky.testproject.tasksystem.exception.TaskNotFoundException;
import com.ilevitsky.testproject.tasksystem.mapper.TaskMapper;
import com.ilevitsky.testproject.tasksystem.mapper.UserMapper;
import com.ilevitsky.testproject.tasksystem.repository.TaskRepository;
import com.ilevitsky.testproject.tasksystem.repository.UserRepository;
import com.ilevitsky.testproject.tasksystem.service.impl.TaskServiceImpl;
import com.ilevitsky.testproject.tasksystem.util.AuthUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @InjectMocks private TaskServiceImpl taskService;

  @Mock private TaskRepository taskRepository;

  @Mock private UserRepository userRepository;

  @Mock private TaskMapper taskMapper;

  @Mock private UserMapper userMapper;

  @Mock private AuthUtil authUtil;

  private static Task task;
  private static TaskDto taskDto;
  private static Task taskND;
  private static TaskDto taskDtoND;

  private static final UUID TASK_ID = UUID.fromString("b9b18de9-971a-488d-95d4-09dfb12d0ec0");
  private static final UUID ASSIGNEE_ID = UUID.randomUUID();
  private static final UUID CREATOR_ID = UUID.randomUUID();

  @BeforeAll
  public static void init() {
    User creator = createTestUser("admin@example.com");
    User assignee = createTestUser("assignee@example.com");

    task = createTestTask(TASK_ID, creator, assignee);
    taskDto = createTestTaskDto(TASK_ID, creator, assignee);
    taskND = createTestTask(null, creator, assignee);
    taskDtoND = createTestTaskDto(null, creator, assignee);
  }

  private static User createTestUser(String email) {
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setEmail(email);
    return user;
  }

  private static Task createTestTask(UUID id, User creator, User assignee) {
    return Task.builder()
        .id(id)
        .title("Test Task")
        .description("Task description")
        .status(TaskStatus.PENDING)
        .priority(TaskPriority.MEDIUM)
        .comments(Set.of("Comment 1"))
        .creator(creator)
        .assignee(assignee)
        .build();
  }

  private static TaskDto createTestTaskDto(UUID id, User creator, User assignee) {
    return TaskDto.builder()
        .id(id)
        .title("Test Task")
        .description("Task description")
        .status(TaskStatus.PENDING.name())
        .priority(TaskPriority.MEDIUM.name())
        .comments(Set.of("Comment 1"))
        .creator(new UserDto(creator.getId(), creator.getEmail(), "ADMIN"))
        .assignee(new UserDto(assignee.getId(), assignee.getEmail(), "USER"))
        .build();
  }

  @Test
  void createTask_ReturnsTaskDto_WhenAdmin() {
    setupAdminUser("admin@example.com");
    setupUserRepository();

    when(taskMapper.mapToEntity(taskDtoND)).thenReturn(taskND);
    when(taskRepository.save(taskND)).thenReturn(task);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.create(taskDtoND);

    assertEquals(taskDto, result);
    verifyInteractionsForCreate();
  }

  @Test
  void createTask_ThrowsException_WhenNotAdmin() {
    when(authUtil.isAdmin()).thenReturn(false);
    assertThrows(OperationDeniedException.class, () -> taskService.create(taskDtoND));
  }

  @Test
  void getAll_ReturnsPagedResponse_WhenAdmin() {
    Pageable pageable = PageRequest.of(0, 10);
    List<Task> tasks = List.of(task);
    Page<Task> page = new PageImpl<>(tasks, pageable, tasks.size());

    when(authUtil.isAdmin()).thenReturn(true);
    when(taskRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result =
        taskService.getAll(
            pageable,
            ASSIGNEE_ID,
            CREATOR_ID,
            TaskStatus.PENDING.name(),
            TaskPriority.MEDIUM.name());

    assertEquals(1, result.getContentList().size());
    assertEquals(taskDto, result.getContentList().get(0));
    verify(taskRepository).findAll(any(Specification.class), eq(pageable));
  }

  @Test
  void getAll_ThrowsException_WhenNotAdmin() {
    when(authUtil.isAdmin()).thenReturn(false);
    when(authUtil.getCurrentUserEmail()).thenReturn("dummy@example.com");

    assertThrows(
        OperationDeniedException.class,
        () ->
            taskService.getAll(
                PageRequest.of(0, 10),
                ASSIGNEE_ID,
                CREATOR_ID,
                TaskStatus.PENDING.name(),
                TaskPriority.MEDIUM.name()));
  }

  @Test
  void getById_ReturnsTaskDto_WhenAdmin() {
    when(authUtil.isAdmin()).thenReturn(true);
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.getById(TASK_ID);

    assertEquals(taskDto, result);
    verify(taskRepository).findById(TASK_ID);
  }

  @Test
  void getById_ReturnsTaskDto_WhenAssignee() {
    simulateAssigneeCheck();

    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.getById(TASK_ID);

    assertEquals(taskDto, result);
    verify(taskRepository).findById(TASK_ID);
  }

  @Test
  void getById_ThrowsException_WhenNotAdminOrAssignee() {
    when(authUtil.isAdmin()).thenReturn(false);
    task.setAssignee(createTestUser("assignee@example.com"));
    when(authUtil.isSameUser("assignee@example.com")).thenReturn(false);
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    assertThrows(OperationDeniedException.class, () -> taskService.getById(TASK_ID));
  }

  @Test
  void update_ReturnsTaskDto_WhenAdmin() {
    when(authUtil.isAdmin()).thenReturn(true);
    setupTaskForUpdate();

    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.createTaskEntityWithoutId(taskDto)).thenReturn(taskND);
    when(taskRepository.save(any(Task.class))).thenReturn(task);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.update(TASK_ID, taskDto);

    assertEquals(taskDto, result);
    verify(taskRepository).save(any(Task.class));
  }

  @Test
  void update_ReturnsTaskDto_WhenNotAdmin() {
    when(authUtil.isAdmin()).thenReturn(false);
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    updateTaskDtoState();

    when(taskMapper.mapToDto(task)).thenReturn(taskDto);
    when(taskRepository.save(task)).thenReturn(task);

    var result = taskService.update(TASK_ID, taskDto);

    assertEquals(taskDto, result);
    verify(taskRepository).save(task);
    verify(taskMapper).mapToDto(task);
  }

  @Test
  void update_ThrowsException_WhenNotFound() {
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());
    assertThrows(TaskNotFoundException.class, () -> taskService.update(TASK_ID, taskDto));
  }

  @Test
  void delete_ThrowsException_WhenNotAdmin() {
    when(authUtil.isAdmin()).thenReturn(false);
    assertThrows(OperationDeniedException.class, () -> taskService.delete(TASK_ID));
  }

  @Test
  void delete_Success_WhenAdmin() {
    when(authUtil.isAdmin()).thenReturn(true);
    taskService.delete(TASK_ID);
    verify(taskRepository).deleteById(TASK_ID);
  }

  @Test
  void addComment_ReturnsTaskDto_WhenAdminOrAssignee() {
    String comment = "New comment";
    setupTaskForComment(comment);

    when(authUtil.isAdmin()).thenReturn(true);
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskRepository.save(task)).thenReturn(task);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.addComment(TASK_ID, new CommentDto(comment));

    assertEquals(taskDto, result);
    assertTrue(task.getComments().contains(comment));
    verify(taskRepository).save(task);
  }

  @Test
  void addComment_ThrowsException_WhenNotAdminOrAssignee() {
    String comment = "New comment";
    when(authUtil.isAdmin()).thenReturn(false);
    task.setAssignee(createTestUser("assignee@example.com"));

    when(authUtil.isSameUser("assignee@example.com")).thenReturn(false);
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    assertThrows(
        OperationDeniedException.class,
        () -> taskService.addComment(TASK_ID, new CommentDto(comment)));
  }

  private void setupAdminUser(String email) {
    when(authUtil.isAdmin()).thenReturn(true);
    User creator = createTestUser(email);
    when(userRepository.findByEmail(authUtil.getCurrentUserEmail()))
        .thenReturn(Optional.of(creator));
  }

  private void setupUserRepository() {
    User assignee = createTestUser("assignee@example.com");
    when(userRepository.findByEmail(assignee.getEmail())).thenReturn(Optional.of(assignee));
  }

  private void setupTaskForUpdate() {
    User assignee = createTestUser("assignee@example.com");
    task.setAssignee(assignee);
    taskDto.setAssignee(new UserDto(assignee.getId(), assignee.getEmail(), "USER"));
  }

  private void setupTaskForComment(String comment) {
    task.setAssignee(createTestUser("assignee@example.com"));
    task.setComments(new HashSet<>());
    task.getComments().add(comment);
  }

  private void simulateAssigneeCheck() {
    when(authUtil.isAdmin()).thenReturn(false);
    when(authUtil.isSameUser("assignee@example.com")).thenReturn(true);
    task.setAssignee(createTestUser("assignee@example.com"));
  }

  private void updateTaskDtoState() {
    taskDto.setStatus(TaskStatus.COMPLETED.name());
    taskDto.setDescription("Updated description");

    task.setStatus(TaskStatus.COMPLETED);
    task.setDescription("Updated description");
  }

  private void verifyInteractionsForCreate() {
    verify(taskRepository).save(taskND);
    verify(taskMapper).mapToEntity(taskDtoND);
    verify(taskMapper).mapToDto(task);
  }
}
