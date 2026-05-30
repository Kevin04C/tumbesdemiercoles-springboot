package com.tumbesdemiercoles.api.columnist.application.usecase;

import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistRequestDto;
import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistResponseDto;
import com.tumbesdemiercoles.api.columnist.application.ports.in.CreateColumnistUseCase;
import com.tumbesdemiercoles.api.columnist.domain.model.Columnist;
import com.tumbesdemiercoles.api.columnist.domain.repository.ColumnistRepository;
import com.tumbesdemiercoles.api.shared.utils.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateColumnistUseCaseImpl implements CreateColumnistUseCase {

  private final ColumnistRepository columnistRepository;

  public Mono<ColumnistResponseDto> execute(ColumnistRequestDto dto) {
    Columnist columnist = Columnist.builder()
        .content(dto.getContent())
        .author(dto.getAuthor())
        .title(dto.getTitle())
        .slug(SlugUtils.toSlug(dto.getTitle()))
        .headline(dto.getHeadline())
        .authorImageUrl(dto.getAuthorImageUrl())
        .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
        .build();

    return columnistRepository.save(columnist)
        .map(this::toResponse);
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
