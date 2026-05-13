package com.tumbesdemiercoles.api.shared.presentation.mapper;

import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import java.util.List;

/**
 * @param <S> El Source (Origen - DTO de la Capa de Aplicación)
 * @param <T> El Target (Destino - DTO de la Capa de Presentación)
 */
public interface PageMapper<S, T> {

  T toResponse(S source);

  default PageResponseDto<T> toPageResponse(PageResponseDto<S> page) {
    if (page == null) {
      return null;
    }

    List<T> responses = page.getContent().stream()
        .map(this::toResponse)
        .toList();

    return PageResponseDto.<T>builder()
        .content(responses)
        .page(page.getPage() + 1)
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .build();
  }
}
