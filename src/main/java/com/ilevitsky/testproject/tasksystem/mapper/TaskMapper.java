package com.ilevitsky.testproject.tasksystem.mapper;

import com.ilevitsky.testproject.tasksystem.dto.TaskDto;
import com.ilevitsky.testproject.tasksystem.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
  protected UserMapper userMapper;

  @Mapping(target = "assignee", expression = "java(userMapper.mapToDto(entity.getAssignee()))")
  @Mapping(target = "creator", expression = "java(userMapper.mapToDto(entity.getCreator()))")
  public abstract TaskDto mapToDto(Task entity);

  @Mapping(target = "assignee", expression = "java(userMapper.mapToEntity(dto.getAssignee()))")
  @Mapping(target = "creator", expression = "java(userMapper.mapToEntity(dto.getCreator()))")
  public abstract Task mapToEntity(TaskDto dto);

  @Mapping(target = "id", ignore = true)
  public abstract Task createTaskEntityWithoutId(TaskDto taskDto);

  @Autowired
  public void setUserMapper(UserMapper userMapper) {
    this.userMapper = userMapper;
  }
}
