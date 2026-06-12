package com.tumbesdemiercoles.api.digitalweekly.application.usecase;

import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyResponseDto;
import com.tumbesdemiercoles.api.digitalweekly.application.ports.in.GetDigitalWeeklyUseCase;
import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeekly;
import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeeklyFilter;
import com.tumbesdemiercoles.api.digitalweekly.domain.repository.DigitalWeeklyRepository;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetDigitalWeeklyUseCaseImpl implements GetDigitalWeeklyUseCase {

  private final DigitalWeeklyRepository digitalWeeklyRepository;

  @Override
  public Mono<DigitalWeeklyResponseDto> getById(UUID id) {
    return digitalWeeklyRepository.findById(id)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("DigitalWeekly", id)))
        .map(this::toResponse);
  }

  @Override
  public Mono<DigitalWeeklyResponseDto> getLatest() {
    return digitalWeeklyRepository.findLatest()
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("DigitalWeekly", "latest")))
        .map(this::toResponse);
  }

  @Override
  public Mono<PageResponseDto<DigitalWeeklyResponseDto>> findDigitalWeeklies(DigitalWeeklyFilter filter) {
    return digitalWeeklyRepository.findDigitalWeeklies(filter)
        .map(paginatedResult -> PageResponseDto.<DigitalWeeklyResponseDto>builder()
            .content(paginatedResult.getContent().stream().map(this::toResponse).toList())
            .page(paginatedResult.getCurrentPage())
            .size(paginatedResult.getPageSize())
            .totalElements(paginatedResult.getTotalElements())
            .totalPages(paginatedResult.getTotalPages())
            .build());
  }

  private DigitalWeeklyResponseDto toResponse(DigitalWeekly digitalWeekly) {
    return DigitalWeeklyResponseDto.builder()
        .id(digitalWeekly.getId())
        .pdfUrl(digitalWeekly.getPdfUrl())
        .frontPageImageUrl(digitalWeekly.getFrontPageImageUrl())
        .descripcion(digitalWeekly.getDescripcion())
        .isActive(digitalWeekly.getIsActive())
        .createdAt(digitalWeekly.getCreatedAt())
        .updatedAt(digitalWeekly.getUpdatedAt())
        .isPremium(digitalWeekly.getIsPremium())
        .url(digitalWeekly.getUrl())
        .build();
  }
}
