package com.ilevitsky.testproject.tasksystem.service.impl;

import com.ilevitsky.testproject.tasksystem.entity.auth.Role;
import com.ilevitsky.testproject.tasksystem.repository.UserRepository;
import com.ilevitsky.testproject.tasksystem.service.UserService;
import com.ilevitsky.testproject.tasksystem.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final AuthUtil authUtil;

  @Override
  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
            .findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      }
    };
  }

  @Override
  public void changeRoleToAdminForUser(String email) {
    if (authUtil.isAdmin()) {
      var user =
          userRepository
              .findByEmail(email)
              .orElseThrow(
                  () ->
                      new UsernameNotFoundException(String.format("No user with email %s", email)));
      user.setRole(Role.ADMIN);
      userRepository.save(user);
    }
  }
}
