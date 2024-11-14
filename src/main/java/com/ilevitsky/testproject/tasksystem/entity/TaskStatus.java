package com.ilevitsky.testproject.tasksystem.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TaskStatus {
  PENDING(1, "Pending"),
  IN_PROCESS(2, "In process"),
  COMPLETED(3, "Completed");

  private final long index;
  private final String lowercaseName;

  TaskStatus(long index, String lowercaseName) {
    this.index = index;
    this.lowercaseName = lowercaseName;
  }

  private static final Map<String, TaskStatus> cachedValues;

  static {
    cachedValues = new HashMap<>();
    for (TaskStatus v : TaskStatus.values()) {
      cachedValues.put(v.lowercaseName, v);
    }
  }

  public static TaskStatus getByLowercaseName(String lowercaseName) {
    return cachedValues.get(lowercaseName);
  }
}
