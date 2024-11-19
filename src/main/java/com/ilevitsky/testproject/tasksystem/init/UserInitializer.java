package com.ilevitsky.testproject.tasksystem.init;

import com.ilevitsky.testproject.tasksystem.dto.payload.SignupRequest;
import com.ilevitsky.testproject.tasksystem.repository.UserRepository;
import com.ilevitsky.testproject.tasksystem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * Contains scripts to initialize users
 */
@Component
@RequiredArgsConstructor
public class UserInitializer implements ApplicationListener<ApplicationReadyEvent> {
  private final UserRepository userRepository;

  private final AuthenticationService authenticationService;

  private final TaskInitializer taskInitializer;
  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    createUsers();
    taskInitializer.initTasks();
  }

  private void createUsers() {
    String[] adminEmails = {"admin1@gmail.com", "admin2@gmail.com", "admin3@gmail.com"};

    for (String email : adminEmails) {
      if (!userRepository.existsByEmail(email)) {
        SignupRequest adminRequest = new SignupRequest();
        adminRequest.setEmail(email);
        adminRequest.setPassword("pass");
        authenticationService.registerAdmin(adminRequest);
      }
    }

    for (int i = 1; i <= 10; i++) {
      String userEmail = "user" + i + "@gmail.com";
      if (!userRepository.existsByEmail(userEmail)) {
        SignupRequest userRequest = new SignupRequest();
        userRequest.setEmail(userEmail);
        userRequest.setPassword("pass");
        authenticationService.register(userRequest);
      }
    }
  }
}
