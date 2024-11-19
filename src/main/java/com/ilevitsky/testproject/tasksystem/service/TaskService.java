package com.ilevitsky.testproject.tasksystem.service;

import com.ilevitsky.testproject.tasksystem.dto.CommentDto;
import com.ilevitsky.testproject.tasksystem.dto.TaskDto;
import com.ilevitsky.testproject.tasksystem.dto.paging.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TaskService {
  PagedResponse<TaskDto> getAll(
      Pageable pageable, String assignee, String creator, String status, String priority);

  TaskDto getById(UUID id);

  TaskDto create(TaskDto dto);

  TaskDto update(UUID id, TaskDto dto);

  void delete(UUID id);

  TaskDto addComment(UUID id, CommentDto comment);
}
