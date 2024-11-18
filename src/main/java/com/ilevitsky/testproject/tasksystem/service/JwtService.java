package com.ilevitsky.testproject.tasksystem.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
  String extractUserName(String token);

  String generateToken(UserDetails userDetails);

  Boolean validateToken(String token, UserDetails userDetails);
}
