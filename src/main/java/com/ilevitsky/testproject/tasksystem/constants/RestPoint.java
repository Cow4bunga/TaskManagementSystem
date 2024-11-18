package com.ilevitsky.testproject.tasksystem.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestPoint {
  public static final String TASK = "/tasks";
  public static final String AUTH = "/auth";
  public static final String USER = "/users";
}
