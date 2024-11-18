package com.ilevitsky.testproject.tasksystem.dto.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
  @NotBlank private String email;
  @NotBlank private String password;
}