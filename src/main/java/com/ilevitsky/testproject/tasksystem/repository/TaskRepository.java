package com.ilevitsky.testproject.tasksystem.repository;

import com.ilevitsky.testproject.tasksystem.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository
    extends JpaRepository<Task, UUID>,
        PagingAndSortingRepository<Task, UUID>,
        JpaSpecificationExecutor<Task> {}
