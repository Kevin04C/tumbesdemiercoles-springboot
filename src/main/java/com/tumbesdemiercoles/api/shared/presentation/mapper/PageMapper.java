package com.tumbesdemiercoles.api.shared.presentation.mapper;

import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * @param <S> El Source (Origen - DTO de la Capa de Aplicación)
 * @param <T> El Target (Destino - DTO de la Capa de Presentación)
 */
public interface PageMapper<S, T> {

  T toResponse(S source);

  /**
   * Mapea un Mono paginado de forma reactiva y sin overhead de conversiones innecesarias.
   */
  default Mono<PageResponseDto<T>> toPageResponse(Mono<PageResponseDto<S>> pageMono) {

    // Usamos map en lugar de flatMap porque no estamos retornando nuevos Publishers internamente
    return pageMono.map(page -> {

      List<S> safeContent = page.getContent() != null ? page.getContent() : Collections.emptyList();

      // Como la lista ya está en memoria y toResponse no hace llamadas a BD/APIs,
      // la transformación nativa es lo más rápido y eficiente.
      List<T> mappedContent = safeContent.stream()
              .map(this::toResponse)
              .toList();

      return PageResponseDto.<T>builder()
              .content(mappedContent)
              .page(page.getPage() + 1)
              .size(page.getSize())
              .totalElements(page.getTotalElements())
              .totalPages(page.getTotalPages())
              .build();
    });
  }
}