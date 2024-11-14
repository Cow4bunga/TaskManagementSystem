package com.ilevitsky.testproject.tasksystem.service.impl;

import com.ilevitsky.testproject.tasksystem.dto.TaskDto;
import com.ilevitsky.testproject.tasksystem.exception.TaskNotFoundException;
import com.ilevitsky.testproject.tasksystem.mapper.TaskMapper;
import com.ilevitsky.testproject.tasksystem.repository.TaskRepository;
import com.ilevitsky.testproject.tasksystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  @Override
  public List<TaskDto> getAll(String status) {
    if (Objects.isNull(status)) {
      return taskRepository.findAll().stream().map(taskMapper::mapToDto).toList();
    }
    return taskRepository.findAll().stream()
        .filter(task -> task.getStatus().getLowercaseName().equals(status))
        .map(taskMapper::mapToDto)
        .toList();
  }

  @Override
  public TaskDto getById(UUID id) {
    return taskRepository
        .findById(id)
        .map(taskMapper::mapToDto)
        .orElseThrow(() -> new TaskNotFoundException(String.format("No task with id %s", id)));
  }

  @Override
  public TaskDto create(TaskDto dto) {
    dto.setId(null);
    return mapAndSave(dto);
  }

  @Override
  public TaskDto update(UUID id, TaskDto dto) {
    if (!taskRepository.existsById(id)) {
      throw new TaskNotFoundException(String.format("No task with id %s", id));
    }
    var task = taskMapper.createTaskEntityWithoutId(dto);
    task.setId(id);

    return taskMapper.mapToDto(taskRepository.save(task));
  }

  @Override
  public void delete(UUID id) {
    taskRepository.deleteById(id);
  }

  private TaskDto mapAndSave(TaskDto dto) {
    return taskMapper.mapToDto(taskRepository.save(taskMapper.mapToEntity(dto)));
  }
}
