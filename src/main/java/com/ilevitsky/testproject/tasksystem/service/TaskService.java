package com.ilevitsky.testproject.tasksystem.service;

import com.ilevitsky.testproject.tasksystem.dto.TaskDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskDto> getAll(String status);
    TaskDto getById(UUID id);
    TaskDto create(TaskDto dto);
    TaskDto update(UUID id, TaskDto dto);
    void delete(UUID id);
}
