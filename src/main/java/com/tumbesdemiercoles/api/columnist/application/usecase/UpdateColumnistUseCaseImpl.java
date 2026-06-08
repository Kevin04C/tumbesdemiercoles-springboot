package com.tumbesdemiercoles.api.columnist.application.usecase;

import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistRequestDto;
import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistResponseDto;
import com.tumbesdemiercoles.api.columnist.application.ports.in.UpdateColumnistUseCase;
import com.tumbesdemiercoles.api.columnist.domain.model.Columnist;
import com.tumbesdemiercoles.api.columnist.domain.repository.ColumnistRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import com.tumbesdemiercoles.api.shared.utils.SlugUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateColumnistUseCaseImpl implements UpdateColumnistUseCase {

  private final ColumnistRepository columnistRepository;

  public Mono<ColumnistResponseDto> execute(UUID id, ColumnistRequestDto dto) {
    return columnistRepository.findById(id)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("Columnist", id)))
        .flatMap(existing -> {
          if (dto.getTitle() == null) {
            return Mono.just(buildUpdated(existing, dto, existing.getSlug()));
          }
          String baseSlug = SlugUtils.toSlug(dto.getTitle());
          return columnistRepository.existsBySlugAndIdNot(baseSlug, id)
              .map(exists -> exists ? baseSlug + "-" + randomHex(6) : baseSlug)
              .map(slug -> buildUpdated(existing, dto, slug));
        })
        .flatMap(columnistRepository::save)
        .map(this::toResponse);
  }

  private static String randomHex(int length) {
    return UUID.randomUUID().toString().replace("-", "").substring(0, length);
  }

  private Columnist buildUpdated(Columnist existing, ColumnistRequestDto dto, String slug) {
    return existing.toBuilder()
        .slug(slug)
        .content(dto.getContent() != null ? dto.getContent() : existing.getContent())
        .author(dto.getAuthor() != null ? dto.getAuthor() : existing.getAuthor())
        .title(dto.getTitle() != null ? dto.getTitle() : existing.getTitle())
        .headline(dto.getHeadline() != null ? dto.getHeadline() : existing.getHeadline())
        .authorImageUrl(dto.getAuthorImageUrl() != null ? dto.getAuthorImageUrl() : existing.getAuthorImageUrl())
        .isActive(dto.getIsActive() != null ? dto.getIsActive() : existing.getIsActive())
        .build();
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
