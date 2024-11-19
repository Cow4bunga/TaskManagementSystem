package com.ilevitsky.testproject.tasksystem.controller;

import com.ilevitsky.testproject.tasksystem.constants.RestPoint;
import com.ilevitsky.testproject.tasksystem.dto.CommentDto;
import com.ilevitsky.testproject.tasksystem.dto.TaskDto;
import com.ilevitsky.testproject.tasksystem.dto.paging.PagedResponse;
import com.ilevitsky.testproject.tasksystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = RestPoint.TASK, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Task controller",
    description = "Controller providing REST API for task management system")
public class TaskController {
  private final TaskService taskService;

  @GetMapping
  @Operation(summary = "Get all tasks.", description = "Fetches list of all tasks.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successful task retrieval request"),
    @ApiResponse(responseCode = "400", description = "Request failure"),
  })
  public ResponseEntity<PagedResponse<TaskDto>> getAll(
      @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable,
      @RequestParam(required = false) String assignee,
      @RequestParam(required = false) String creator,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String priority) {
    return new ResponseEntity<>(
        taskService.getAll(pageable, assignee, creator, status, priority), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get task by id.", description = "Fetches task with corresponding id.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully retrieved one task by id"),
    @ApiResponse(responseCode = "400", description = "Request failure"),
  })
  public ResponseEntity<TaskDto> getById(@PathVariable(name = "id") UUID taskId) {
    return new ResponseEntity<>(taskService.getById(taskId), HttpStatus.OK);
  }

  @PostMapping
  @Operation(summary = "Create new task", description = "Creates new task. Returns DTO.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Successfully created new task"),
    @ApiResponse(responseCode = "400", description = "Request failure"),
  })
  public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto) {
    return new ResponseEntity<>(taskService.create(taskDto), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update existing task by id.",
      description = "Updates existing task state and returns DTO.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully updated one task by id"),
    @ApiResponse(responseCode = "400", description = "Request failure"),
  })
  public ResponseEntity<TaskDto> updateById(
      @PathVariable(name = "id") UUID taskId, @RequestBody TaskDto taskDto) {
    return new ResponseEntity<>(taskService.update(taskId, taskDto), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete task by id.",
      description = "Deletes task with corresponding id. Returns void.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Successfully deleted one task by id"),
    @ApiResponse(responseCode = "400", description = "Request failure"),
  })
  public ResponseEntity<Void> deleteById(@PathVariable(name = "id") UUID taskId) {
    taskService.delete(taskId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PutMapping("/{id}/comment")
  @Operation(
      summary = "Update existing task by id. Add comment on task",
      description = "Updates existing task comments and returns nothing.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Successfully updated one task by id"),
    @ApiResponse(responseCode = "400", description = "Request failure"),
  })
  public ResponseEntity<TaskDto> updateById(
      @PathVariable(name = "id") UUID taskId, @RequestBody CommentDto comment) {
    return new ResponseEntity<>(taskService.addComment(taskId, comment), HttpStatus.OK);
  }
}
