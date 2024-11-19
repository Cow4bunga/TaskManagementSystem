package com.ilevitsky.testproject.tasksystem.controller;

import com.ilevitsky.testproject.tasksystem.constants.RestPoint;
import com.ilevitsky.testproject.tasksystem.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RestPoint.USER, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "User controller",
    description = "Controller providing users functionality for task management system")
public class UserController {
  private final UserService userService;

  @PostMapping("/toAdmin")
  ResponseEntity<Void> upgradeRoleForUser(@RequestBody String email) {
    userService.changeRoleToAdminForUser(email);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
