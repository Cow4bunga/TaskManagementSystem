package com.ilevitsky.testproject.tasksystem.spec;

import com.ilevitsky.testproject.tasksystem.entity.Task;
import com.ilevitsky.testproject.tasksystem.entity.TaskPriority;
import com.ilevitsky.testproject.tasksystem.entity.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecificationBuilder {
  private final List<Specification<Task>> specs = new ArrayList<>();

  public TaskSpecificationBuilder byStatus(TaskStatus status) {
    specs.add((r, q, cB) -> cB.equal(r.<String>get("status"), status.name()));
    return this;
  }

  public TaskSpecificationBuilder byPriority(TaskPriority priority) {
    specs.add((r, q, cB) -> cB.equal(r.get("priority").as(String.class), priority.name()));
    return this;
  }

  public TaskSpecificationBuilder createdBy(String creator) {
    specs.add((r, q, cB) -> cB.equal(r.join("creator").get("email").as(String.class), creator));
    return this;
  }

  public TaskSpecificationBuilder assignedTo(String assignee) {
    specs.add((r, q, cB) -> cB.equal(r.join("assignee").get("email").as(String.class), assignee));
    return this;
  }

  public Specification<Task> build() {
    return specs.stream()
        .reduce((accumulated, toAdd) -> Specification.where(accumulated).and(toAdd))
        .orElse(Specification.where(null));
  }
}
