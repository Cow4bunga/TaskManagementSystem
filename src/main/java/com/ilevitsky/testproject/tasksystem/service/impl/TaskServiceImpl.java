package com.ilevitsky.testproject.tasksystem.service.impl;

import com.ilevitsky.testproject.tasksystem.dto.CommentDto;
import com.ilevitsky.testproject.tasksystem.dto.TaskDto;
import com.ilevitsky.testproject.tasksystem.dto.paging.PageInfo;
import com.ilevitsky.testproject.tasksystem.dto.paging.PagedResponse;
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
import com.ilevitsky.testproject.tasksystem.service.TaskService;
import com.ilevitsky.testproject.tasksystem.spec.TaskSpecificationBuilder;
import com.ilevitsky.testproject.tasksystem.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final TaskMapper taskMapper;
  private final UserMapper userMapper;
  private final AuthUtil authUtil;

  @Override
  public PagedResponse<TaskDto> getAll(
      Pageable pageable, UUID assignee, UUID creator, String status, String priority) {
    if (authUtil.isAdmin()
        || (!Objects.isNull(assignee)
            && authUtil
                .getCurrentUserEmail()
                .equals(userRepository.findById(assignee).orElse(new User()).getEmail()))) {
      var specBuilder = new TaskSpecificationBuilder();

      if (!Objects.isNull(assignee)) {
        specBuilder.assignedTo(assignee);
      }
      if (!Objects.isNull(creator)) {
        specBuilder.createdBy(creator);
      }
      if (!Objects.isNull(status)) {
        specBuilder.byStatus(TaskStatus.valueOf(status.toUpperCase()));
      }
      if (!Objects.isNull(priority)) {
        specBuilder.byPriority(TaskPriority.valueOf(priority.toUpperCase()));
      }

      var spec = specBuilder.build();

      Page<Task> page = taskRepository.findAll(spec, pageable);

      return new PagedResponse<>(
          page.getContent().stream().map(taskMapper::mapToDto).toList(),
          new PageInfo(
              page.getNumber(),
              page.getSize(),
              page.getTotalElements(),
              page.getTotalPages(),
              page.isLast()));
    } else {
      log.error("Get all tasks: Access denied for {}", authUtil.getCurrentUserEmail());
      throw new OperationDeniedException(
          "Forbidden operation for current user! Must be admin to get all tasks.");
    }
  }

  @Override
  public TaskDto getById(UUID id) {
    var task =
        taskRepository
            .findById(id)
            .orElseThrow(() -> new TaskNotFoundException(String.format("No task with id %s", id)));
    if (authUtil.isAdmin() || authUtil.isSameUser(task.getAssignee().getEmail())) {
      return taskMapper.mapToDto(task);
    } else {
      log.error("Get task: Access denied for {}", authUtil.getCurrentUserEmail());
      throw new OperationDeniedException(
          "Forbidden operation for current user! Must be admin or task assignee to view task.");
    }
  }

  @Override
  public TaskDto create(TaskDto dto) {
    if (authUtil.isAdmin()) {
      dto.setId(null);
      var creator =
          userRepository
              .findByEmail(authUtil.getCurrentUserEmail())
              .orElseThrow(
                  () ->
                      new UsernameNotFoundException(
                          String.format(
                              "No admin with email %s, unable to create task.",
                              dto.getCreator().getEmail())));
      dto.setCreator(userMapper.mapToDto(creator));

      var assignee = userRepository.findByEmail(dto.getAssignee().getEmail()).orElse(null);
      dto.setAssignee(userMapper.mapToDto(assignee));

      return mapAndSave(dto);
    } else {
      log.error("Create: Access denied for {}", authUtil.getCurrentUserEmail());
      throw new OperationDeniedException(
          "Forbidden operation for current user! Must be admin to create tasks.");
    }
  }

  @Override
  public TaskDto update(UUID id, TaskDto dto) {
    var task =
        taskRepository
            .findById(id)
            .orElseThrow(() -> new TaskNotFoundException(String.format("No task with id %s", id)));

    if (authUtil.isAdmin()) {
      var newTask = taskMapper.createTaskEntityWithoutId(dto);
      newTask.setId(id);
      newTask.setCreator(task.getCreator());

      var assignee = userRepository.findByEmail(dto.getAssignee().getEmail()).orElse(null);

      newTask.setAssignee(assignee);
      return taskMapper.mapToDto(taskRepository.save(newTask));
    } else {
      task.setStatus(TaskStatus.valueOf(dto.getStatus()));
      task.setDescription(dto.getDescription());
    }

    task.setId(id);

    return taskMapper.mapToDto(taskRepository.save(task));
  }

  @Override
  public void delete(UUID id) {
    if (authUtil.isAdmin()) {
      taskRepository.deleteById(id);
    } else {
      log.error("Delete: Access denied for {}", authUtil.getCurrentUserEmail());
      throw new OperationDeniedException(
          "Forbidden operation for current user! Must be admin to delete tasks.");
    }
  }

  @Override
  public TaskDto addComment(UUID id, CommentDto comment) {
    var task =
        taskRepository
            .findById(id)
            .orElseThrow(() -> new TaskNotFoundException(String.format("No task with id %s", id)));

    if (authUtil.isAdmin() || authUtil.isSameUser(task.getAssignee().getEmail())) {
      var comments = task.getComments();
      comments.add(comment.getComment());
      task.setComments(comments);

      return taskMapper.mapToDto(taskRepository.save(task));
    } else {
      log.error("Add comment: Access denied for {}", authUtil.getCurrentUserEmail());
      throw new OperationDeniedException(
          "Forbidden operation for current user. Must be admin or task assignee to add comments.");
    }
  }

  private TaskDto mapAndSave(TaskDto dto) {
    return taskMapper.mapToDto(taskRepository.save(taskMapper.mapToEntity(dto)));
  }
}
