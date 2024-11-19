package com.ilevitsky.testproject.tasksystem.mapper;

import com.ilevitsky.testproject.tasksystem.dto.UserDto;
import com.ilevitsky.testproject.tasksystem.entity.auth.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto mapToDto(User entity);

  User mapToEntity(UserDto dto);
}
