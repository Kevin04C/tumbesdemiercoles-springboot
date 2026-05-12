package com.tumbesdemiercoles.api.shared.presentation.mapper;

import com.tumbesdemiercoles.api.shared.dto.PageResponseDto;
import java.util.List;

/**
 * @param <D> El modelo de Dominio (Ej. User)
 * @param <R> El DTO de Respuesta (Ej. UserResponse)
 */
public interface PageMapper<D, R> {

  // Todo mapper específico tendrá que saber cómo traducir 1 elemento individual.
  // MapStruct generará la implementación de este método automáticamente.
  R toResponse(D domain);

  // Este método ya viene "gratis" para cualquier mapper que herede de esta interfaz.
  default PageResponseDto<R> toPageResponse(PageResponseDto<D> page) {
    if (page == null) {
      return null;
    }

    List<R> responses = page.getContent().stream()
        .map(this::toResponse)
        .toList();

    return PageResponseDto.<R>builder()
        .content(responses)
        .page(page.getPage())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .build();
  }
}
