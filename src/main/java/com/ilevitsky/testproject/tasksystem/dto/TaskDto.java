package com.ilevitsky.testproject.tasksystem.dto;

import com.ilevitsky.testproject.tasksystem.entity.TaskPriority;
import com.ilevitsky.testproject.tasksystem.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
  private UUID id;

  private String title;

  private String description;

  private TaskStatus status;

  private TaskPriority priority;

  private Set<String> comments;
}
