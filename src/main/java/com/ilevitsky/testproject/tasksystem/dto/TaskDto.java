package com.ilevitsky.testproject.tasksystem.dto;

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

  private String status;

  private String priority;

  private Set<String> comments;

  private UserDto assignee;

  private UserDto creator;
}
