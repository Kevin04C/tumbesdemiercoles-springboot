package com.tumbesdemiercoles.api.entities.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PageResponseDto<T> {
  private List<T> content;
  private int page;
  private int size;
  private long totalElements;
  private long totalPages;
}
