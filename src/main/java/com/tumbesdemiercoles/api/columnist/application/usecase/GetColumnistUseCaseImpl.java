package com.tumbesdemiercoles.api.columnist.application.usecase;

import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistResponseDto;
import com.tumbesdemiercoles.api.columnist.application.ports.in.GetColumnistUseCase;
import com.tumbesdemiercoles.api.columnist.domain.model.Columnist;
import com.tumbesdemiercoles.api.columnist.domain.model.ColumnistFilter;
import com.tumbesdemiercoles.api.columnist.domain.repository.ColumnistRepository;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetColumnistUseCaseImpl implements GetColumnistUseCase {

  private final ColumnistRepository columnistRepository;

  public Mono<ColumnistResponseDto> getById(UUID id) {
    return columnistRepository.findById(id)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("Columnist", id)))
        .map(this::toResponse);
  }

  @Override
  public Mono<ColumnistResponseDto> getBySlug(String slug) {
    return columnistRepository.findBySlug(slug)
        .map(this::toResponse);
  }

  @Override
  public Mono<PageResponseDto<ColumnistResponseDto>> findColumnists(ColumnistFilter filter) {
    return columnistRepository.findColumnists(filter)
        .map(paginatedResult -> PageResponseDto.<ColumnistResponseDto>builder()
            .content(paginatedResult.getContent().stream().map(this::toResponse).toList())
            .page(paginatedResult.getCurrentPage())
            .size(paginatedResult.getPageSize())
            .totalElements(paginatedResult.getTotalElements())
            .totalPages(paginatedResult.getTotalPages())
            .build());
  }

  private ColumnistResponseDto toResponse(Columnist columnist) {
    return ColumnistResponseDto.builder()
        .id(columnist.getId())
        .content(columnist.getContent())
        .author(columnist.getAuthor())
        .title(columnist.getTitle())
        .slug(columnist.getSlug())
        .headline(columnist.getHeadline())
        .authorImageUrl(columnist.getAuthorImageUrl())
        .isActive(columnist.getIsActive())
        .createdAt(columnist.getCreatedAt())
        .updatedAt(columnist.getUpdatedAt())
        .build();
  }
}
