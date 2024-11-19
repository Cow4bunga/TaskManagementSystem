package com.ilevitsky.testproject.tasksystem.spec;

import com.ilevitsky.testproject.tasksystem.entity.Task;
import com.ilevitsky.testproject.tasksystem.entity.TaskPriority;
import com.ilevitsky.testproject.tasksystem.entity.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

  public TaskSpecificationBuilder createdBy(UUID creator) {
    specs.add(
        (r, q, cB) ->
            cB.equal(r.join("creator").get("id").as(String.class), String.valueOf(creator)));
    return this;
  }

  public TaskSpecificationBuilder assignedTo(UUID assignee) {
    specs.add(
        (r, q, cB) ->
            cB.equal(r.join("assignee").get("id").as(String.class), String.valueOf(assignee)));
    return this;
  }

  public Specification<Task> build() {
    return specs.stream()
        .reduce((accumulated, toAdd) -> Specification.where(accumulated).and(toAdd))
        .orElse(Specification.where(null));
  }
}
