package com.tumbesdemiercoles.api.shared.domain.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Objeto puro de dominio para transportar listas de datos paginadas
 * desde la Infraestructura (Base de datos) hacia la Aplicación (Casos de Uso).
 */
@Getter
@Builder
@AllArgsConstructor
public class PaginatedResult<T> {

  private final List<T> content;
  private final int currentPage;
  private final int pageSize;
  private final long totalElements;
  private final int totalPages;

  public boolean hasNext() {
    return currentPage < totalPages - 1;
  }

  public boolean hasPrevious() {
    return currentPage > 0;
  }
}
