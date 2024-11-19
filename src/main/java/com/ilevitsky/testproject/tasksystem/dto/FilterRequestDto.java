package com.ilevitsky.testproject.tasksystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequestDto {
  private String fieldName;
  private String operator;
  private String value;
}
