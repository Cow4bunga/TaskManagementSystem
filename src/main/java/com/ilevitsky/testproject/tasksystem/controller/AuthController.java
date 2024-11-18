package com.ilevitsky.testproject.tasksystem.controller;

import com.ilevitsky.testproject.tasksystem.constants.RestPoint;
import com.ilevitsky.testproject.tasksystem.dto.payload.JwtResponse;
import com.ilevitsky.testproject.tasksystem.dto.payload.LoginRequest;
import com.ilevitsky.testproject.tasksystem.dto.payload.SignupRequest;
import com.ilevitsky.testproject.tasksystem.entity.auth.User;
import com.ilevitsky.testproject.tasksystem.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RestPoint.AUTH, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Authentication controller",
    description = "Controller providing authentication for task management system")
public class AuthController {
  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody SignupRequest registerRequest) {
    return ResponseEntity.ok(authenticationService.register(registerRequest));
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(authenticationService.login(loginRequest));
  }
}
