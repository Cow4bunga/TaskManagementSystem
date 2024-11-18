package com.ilevitsky.testproject.tasksystem.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
  private static final String ROLE_ADMIN = "ADMIN";
  private static final String ROLE_USER = "USER";

  public boolean isAdmin() {
    var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    return authorities.contains(new SimpleGrantedAuthority(ROLE_ADMIN));
  }

  public boolean isUser() {
    var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    return authorities.contains(new SimpleGrantedAuthority(ROLE_USER));
  }

  public boolean isSameUser(String email) {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    return email.equals(auth.getName());
  }

  public String getCurrentUserEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
