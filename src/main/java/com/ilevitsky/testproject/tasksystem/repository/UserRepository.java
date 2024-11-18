package com.ilevitsky.testproject.tasksystem.repository;

import com.ilevitsky.testproject.tasksystem.entity.auth.Role;
import com.ilevitsky.testproject.tasksystem.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);

  Optional<User> findByRole(Role role);
}