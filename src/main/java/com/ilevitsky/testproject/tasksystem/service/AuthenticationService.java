package com.ilevitsky.testproject.tasksystem.service;

import com.ilevitsky.testproject.tasksystem.dto.payload.JwtResponse;
import com.ilevitsky.testproject.tasksystem.dto.payload.LoginRequest;
import com.ilevitsky.testproject.tasksystem.dto.payload.SignupRequest;
import com.ilevitsky.testproject.tasksystem.entity.auth.User;

public interface AuthenticationService {
  User register(SignupRequest registerRequest);

  JwtResponse login(LoginRequest loginRequest);

  User registerAdmin(SignupRequest signupRequest);
}
