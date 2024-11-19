package com.ilevitsky.testproject.tasksystem.spec;

import com.ilevitsky.testproject.tasksystem.dto.FilterRequestDto;
import com.ilevitsky.testproject.tasksystem.entity.Task;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class TaskSpecification implements Specification<Task> {
  private FilterRequestDto filterCriteria;

  @Override
  public Predicate toPredicate(
      Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return switch (filterCriteria.getOperator()) {
      case "eq" ->
          criteriaBuilder.equal(
              root.get(filterCriteria.getFieldName()).as(String.class), filterCriteria.getValue());
      case "gte" ->
          criteriaBuilder.greaterThanOrEqualTo(
              root.get(filterCriteria.getFieldName()), filterCriteria.getValue());
      case "lte" ->
          criteriaBuilder.lessThanOrEqualTo(
              root.get(filterCriteria.getFieldName()), filterCriteria.getValue());
      case null, default -> null;
    };
  }
}
