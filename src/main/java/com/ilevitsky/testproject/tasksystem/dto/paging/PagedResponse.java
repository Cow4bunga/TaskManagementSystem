package com.ilevitsky.testproject.tasksystem.dto.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
  private List<T> contentList;
  private PageInfo page;
}
