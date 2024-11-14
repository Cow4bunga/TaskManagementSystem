package com.ilevitsky.testproject.tasksystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "title")
  @NotBlank(message = "Title should not be blank")
  @Size(min = 1, max = 32, message = "Minimum length is 1 symbol, maximum is 32.")
  private String title;

  @Column(name = "description")
  @Size(min = 1, max = 128, message = "Minimum length is 1 symbol, maximum is 128.")
  private String description;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @Column(name = "priority")
  @Enumerated(EnumType.STRING)
  private TaskPriority priority;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "task_comments", joinColumns = @JoinColumn(name = "task_id"))
  @Column(name = "comment")
  private Set<String> comments;
}
