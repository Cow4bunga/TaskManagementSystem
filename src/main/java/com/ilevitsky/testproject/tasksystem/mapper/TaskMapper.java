package com.ilevitsky.testproject.tasksystem.mapper;

import com.ilevitsky.testproject.tasksystem.dto.TaskDto;
import com.ilevitsky.testproject.tasksystem.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
  TaskDto mapToDto(Task entity);

  Task mapToEntity(TaskDto dto);

  @Mapping(target = "id", ignore = true)
  Task createTaskEntityWithoutId(TaskDto taskDto);
}
