package com.tumbesdemiercoles.api.shared.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para representar información de paginación en las respuestas de la API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationInfo {

  private int page;
  private int size;
  private long totalElements;
  private int totalPages;

  public static PaginationInfo of(int page, int size, long totalElements) {
    int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
    return PaginationInfo.builder()
        .page(page)
        .size(size)
        .totalElements(totalElements)
        .totalPages(totalPages)
        .build();
  }

}
