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

  @BeforeAll
  public static void init() {
    User creator = new User();
    creator.setId(UUID.randomUUID());
    creator.setEmail("admin@example.com");

    User assignee = new User();
    assignee.setId(UUID.randomUUID());
    assignee.setEmail("assignee@example.com");

    task =
        Task.builder()
            .id(TASK_ID)
            .title("Test Task")
            .description("Task description")
            .status(TaskStatus.PENDING)
            .priority(TaskPriority.MEDIUM)
            .comments(Set.of("Comment 1"))
            .creator(creator)
            .assignee(assignee)
            .build();

    taskDto =
        TaskDto.builder()
            .id(TASK_ID)
            .title("Test Task")
            .description("Task description")
            .status(TaskStatus.PENDING.name())
            .priority(TaskPriority.MEDIUM.name())
            .comments(Set.of("Comment 1"))
            .creator(new UserDto(creator.getId(), creator.getEmail(), "ADMIN"))
            .assignee(new UserDto(assignee.getId(), assignee.getEmail(), "USER"))
            .build();

    taskND =
        Task.builder()
            .title("Test Task")
            .description("Task description")
            .status(TaskStatus.PENDING)
            .priority(TaskPriority.MEDIUM)
            .comments(Set.of("Comment 1"))
            .creator(creator)
            .assignee(assignee)
            .build();

    taskDtoND =
        TaskDto.builder()
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
  void TaskService_CreateTask_ReturnsTaskDto_WhenAdmin() {
    when(authUtil.isAdmin()).thenReturn(true);

    User creator = new User();
    creator.setId(UUID.randomUUID());
    creator.setEmail("admin@example.com");
    when(userRepository.findByEmail(creator.getEmail())).thenReturn(Optional.of(creator));

    User assignee = new User();
    assignee.setId(UUID.randomUUID());
    assignee.setEmail("assignee@example.com");
    when(userRepository.findByEmail(assignee.getEmail())).thenReturn(Optional.of(assignee));

    taskDtoND.setCreator(new UserDto(creator.getId(), creator.getEmail(), "ADMIN"));
    taskDtoND.setAssignee(new UserDto(assignee.getId(), assignee.getEmail(), "USER"));

    when(taskMapper.mapToEntity(taskDtoND)).thenReturn(taskND);
    when(taskRepository.save(taskND)).thenReturn(task);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.create(taskDtoND);

    assertEquals(taskDto, result);
    verify(taskRepository, times(1)).save(taskND);
  }

  @Test
  void TaskService_CreateTask_ThrowsException_WhenNotAdmin() {
    when(authUtil.isAdmin()).thenReturn(false);

    assertThrows(OperationDeniedException.class, () -> taskService.create(taskDtoND));
  }

  @Test
  void TaskService_GetAll_ReturnsPagedResponse_WhenAdmin() {
    Pageable pageable = PageRequest.of(0, 10);
    List<Task> tasks = List.of(task);
    Page<Task> page = new PageImpl<>(tasks, pageable, tasks.size());

    when(authUtil.isAdmin()).thenReturn(true);
    when(taskRepository.findAll(pageable)).thenReturn(page);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.getAll(pageable);

    assertEquals(1, result.getContentList().size());
    assertEquals(taskDto, result.getContentList().get(0));
    assertEquals(0, result.getPage().getPageNo());
    assertEquals(10, result.getPage().getPageSize());
    assertEquals(1, result.getPage().getTotalElements());
    assertEquals(1, result.getPage().getTotalPages());
    assertTrue(result.getPage().isLast());

    verify(taskRepository, times(1)).findAll(pageable);
  }

  @Test
  void TaskService_GetAll_ThrowsException_WhenNotAdmin() {
    when(authUtil.isAdmin()).thenReturn(false);

    assertThrows(OperationDeniedException.class, () -> taskService.getAll(PageRequest.of(0, 10)));
  }

  @Test
  void TaskService_GetById_ReturnsTaskDto_WhenAdmin() {
    when(authUtil.isAdmin()).thenReturn(true);
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.getById(TASK_ID);

    assertEquals(taskDto, result);
    verify(taskRepository, times(1)).findById(TASK_ID);
  }

  @Test
  void TaskService_GetById_ReturnsTaskDto_WhenAssignee() {
    when(authUtil.isAdmin()).thenReturn(false);
    when(authUtil.isSameUser("assignee@example.com")).thenReturn(true);

    User assignee = new User();
    assignee.setEmail("assignee@example.com");

    task.setAssignee(assignee);

    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.getById(TASK_ID);

    assertEquals(taskDto, result);
    verify(taskRepository, times(1)).findById(TASK_ID);
  }

  @Test
  void TaskService_GetById_ThrowsException_WhenNotAdminOrAssignee() {
    when(authUtil.isAdmin()).thenReturn(false);

    User assignee = new User();
    assignee.setEmail("assignee@example.com");
    task.setAssignee(assignee);

    when(authUtil.isSameUser(assignee.getEmail())).thenReturn(false);
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    assertThrows(OperationDeniedException.class, () -> taskService.getById(TASK_ID));
  }

  @Test
  void TaskService_Update_ReturnsTaskDto_WhenAdmin() {
    when(authUtil.isAdmin()).thenReturn(true);

    User assignee = new User();
    assignee.setEmail("assignee@example.com");
    task.setAssignee(assignee);

    UserDto assigneeDto = new UserDto();
    assigneeDto.setEmail(assignee.getEmail());
    taskDto.setAssignee(assigneeDto);

    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.createTaskEntityWithoutId(taskDto)).thenReturn(taskND);
    when(taskRepository.save(any(Task.class))).thenReturn(task);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.update(TASK_ID, taskDto);

    assertEquals(taskDto, result);
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Test
  void TaskService_Update_ReturnsTaskDto_WhenNotAdmin() {
    when(authUtil.isAdmin()).thenReturn(false);
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    taskDto.setStatus(TaskStatus.COMPLETED.name());
    taskDto.setDescription("Updated description");

    task.setStatus(TaskStatus.COMPLETED);
    task.setDescription("Updated description");

    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.update(TASK_ID, taskDto);

    assertEquals(taskDto, result);
    assertEquals(TaskStatus.COMPLETED, task.getStatus());
    verify(taskRepository, times(1)).save(task);
  }

  @Test
  void TaskService_Update_ThrowsException_WhenNotFound() {
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> taskService.update(TASK_ID, taskDto));
  }

  @Test
  void TaskService_Delete_ThrowsException_WhenNotAdmin() {
    when(authUtil.isAdmin()).thenReturn(false);

    assertThrows(OperationDeniedException.class, () -> taskService.delete(TASK_ID));
  }

  @Test
  void TaskService_Delete_Success_WhenAdmin() {
    when(authUtil.isAdmin()).thenReturn(true);

    taskService.delete(TASK_ID);

    verify(taskRepository, times(1)).deleteById(TASK_ID);
  }

  @Test
  void TaskService_AddComment_ReturnsTaskDto_WhenAdminOrAssignee() {
    String comment = "New comment";

    User assignee = new User();
    assignee.setEmail("assignee@example.com");
    task.setAssignee(assignee);
    task.setComments(new HashSet<>());

    when(authUtil.isAdmin()).thenReturn(true);
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskRepository.save(task)).thenReturn(task);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.addComment(TASK_ID, new CommentDto(comment));

    assertEquals(taskDto, result);
    assertTrue(task.getComments().contains(comment));
    verify(taskRepository, times(1)).save(task);
  }

  @Test
  void TaskService_AddComment_ThrowsException_WhenNotAdminOrAssignee() {
    String comment = "New comment";
    when(authUtil.isAdmin()).thenReturn(false);

    User assignee = new User();
    assignee.setEmail("assignee@example.com");
    task.setAssignee(assignee);

    when(authUtil.isSameUser(assignee.getEmail())).thenReturn(false);
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    assertThrows(
        OperationDeniedException.class,
        () -> taskService.addComment(TASK_ID, new CommentDto(comment)));
  }
}
