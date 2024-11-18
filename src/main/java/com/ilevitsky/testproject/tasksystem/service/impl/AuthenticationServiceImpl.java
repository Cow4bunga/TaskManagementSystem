package com.ilevitsky.testproject.tasksystem.service.impl;

import com.ilevitsky.testproject.tasksystem.dto.payload.JwtResponse;
import com.ilevitsky.testproject.tasksystem.dto.payload.LoginRequest;
import com.ilevitsky.testproject.tasksystem.dto.payload.SignupRequest;
import com.ilevitsky.testproject.tasksystem.entity.auth.Role;
import com.ilevitsky.testproject.tasksystem.entity.auth.User;
import com.ilevitsky.testproject.tasksystem.repository.UserRepository;
import com.ilevitsky.testproject.tasksystem.service.AuthenticationService;
import com.ilevitsky.testproject.tasksystem.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Override
  public User register(SignupRequest registerRequest) {
    User user = new User();

    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setRole(Role.USER);

    return userRepository.save(user);
  }

  @Override
  public JwtResponse login(LoginRequest loginRequest) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(), loginRequest.getPassword()));

    var user =
        userRepository
            .findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    var jwt = jwtService.generateToken(user);

    JwtResponse jwtAuthenticationResponse = new JwtResponse();
    jwtAuthenticationResponse.setAccessToken(jwt);
    return jwtAuthenticationResponse;
  }
}
