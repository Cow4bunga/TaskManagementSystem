package com.ilevitsky.testproject.tasksystem.service;

import com.ilevitsky.testproject.tasksystem.dto.TaskDto;
import com.ilevitsky.testproject.tasksystem.entity.Task;
import com.ilevitsky.testproject.tasksystem.entity.TaskPriority;
import com.ilevitsky.testproject.tasksystem.entity.TaskStatus;
import com.ilevitsky.testproject.tasksystem.mapper.TaskMapper;
import com.ilevitsky.testproject.tasksystem.repository.TaskRepository;
import com.ilevitsky.testproject.tasksystem.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
  @InjectMocks private TaskServiceImpl taskService;
  @Mock private TaskRepository taskRepository;
  @Mock private TaskMapper taskMapper;

  private static Task task;
  private static TaskDto taskDto;
  private static Task taskND;
  private static TaskDto taskDtoND;

  private static final UUID TASK_ID = UUID.fromString("b9b18de9-971a-488d-95d4-09dfb12d0ec0");

  @BeforeAll
  public static void init() {
    task =
        Task.builder()
            .id(TASK_ID)
            .title("Test Task")
            .description("Task description")
            .status(TaskStatus.PENDING)
            .priority(TaskPriority.MEDIUM)
            .comments(Set.of("Comment 1"))
            .build();

    taskDto =
        TaskDto.builder()
            .id(TASK_ID)
            .title("Test Task")
            .description("Task description")
            .status(TaskStatus.PENDING)
            .priority(TaskPriority.MEDIUM)
            .comments(Set.of("Comment 1"))
            .build();

    taskND =
        Task.builder()
            .title("Test Task")
            .description("Task description")
            .status(TaskStatus.PENDING)
            .priority(TaskPriority.MEDIUM)
            .comments(Set.of("Comment 1"))
            .build();

    taskDtoND =
        TaskDto.builder()
            .title("Test Task")
            .description("Task description")
            .status(TaskStatus.PENDING)
            .priority(TaskPriority.MEDIUM)
            .comments(Set.of("Comment 1"))
            .build();
  }

  @Test
  void TaskService_CreateTask_ReturnsTaskDto() {
    when(taskMapper.mapToEntity(taskDtoND)).thenReturn(taskND);
    when(taskRepository.save(taskND)).thenReturn(task);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.create(taskDtoND);

    assertEquals(taskDto, result);
    verify(taskRepository, times(1)).save(taskND);
  }

  @Test
  void TaskService_GetAll_ReturnsTaskDtoList() {
    List<Task> tasks = List.of(task);
    when(taskRepository.findAll()).thenReturn(tasks);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.getAll(null);

    assertEquals(1, result.size());
    assertEquals(taskDto, result.getFirst());
    verify(taskRepository, times(1)).findAll();
  }

  @Test
  void TaskService_GetById_ReturnsTaskDto() {
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.getById(TASK_ID);

    assertEquals(taskDto, result);
    verify(taskRepository, times(1)).findById(TASK_ID);
  }

  @Test
  void TaskService_Update_ReturnsTaskDto() {
    when(taskRepository.existsById(TASK_ID)).thenReturn(true);
    when(taskMapper.createTaskEntityWithoutId(taskDto)).thenReturn(taskND);
    when(taskRepository.save(taskND)).thenReturn(task);
    when(taskMapper.mapToDto(task)).thenReturn(taskDto);

    var result = taskService.update(TASK_ID, taskDto);

    assertEquals(taskDto, result);
    verify(taskRepository, times(1)).save(taskND);
  }

  @Test
  void TaskService_Delete_ReturnsNothing() {
    taskService.delete(TASK_ID);
    verify(taskRepository, times(1)).deleteById(TASK_ID);
  }
}
