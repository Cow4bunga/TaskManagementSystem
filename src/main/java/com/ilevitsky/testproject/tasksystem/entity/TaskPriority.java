package com.ilevitsky.testproject.tasksystem.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TaskPriority {
  LOW(1, "Low"),
  MEDIUM(2, "Medium"),
  HIGH(3, "High");

  private final long index;
  private final String lowercaseName;

  TaskPriority(long index, String lowercaseName) {
    this.index = index;
    this.lowercaseName = lowercaseName;
  }

  private static final Map<String, TaskPriority> cachedValues;

  static {
    cachedValues = new HashMap<>();
    for (TaskPriority v : TaskPriority.values()) {
      cachedValues.put(v.lowercaseName, v);
    }
  }

  public static TaskPriority getByLowercaseName(String lowercaseName) {
    return cachedValues.get(lowercaseName);
  }
}
