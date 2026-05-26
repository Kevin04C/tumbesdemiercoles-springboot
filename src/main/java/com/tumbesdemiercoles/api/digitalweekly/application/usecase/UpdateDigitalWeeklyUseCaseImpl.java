package com.tumbesdemiercoles.api.digitalweekly.application.usecase;

import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyRequestDto;
import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyResponseDto;
import com.tumbesdemiercoles.api.digitalweekly.application.ports.in.UpdateDigitalWeeklyUseCase;
import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeekly;
import com.tumbesdemiercoles.api.digitalweekly.domain.repository.DigitalWeeklyRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateDigitalWeeklyUseCaseImpl implements UpdateDigitalWeeklyUseCase {

  private final DigitalWeeklyRepository digitalWeeklyRepository;

  @Override
  public Mono<DigitalWeeklyResponseDto> execute(UUID id, DigitalWeeklyRequestDto dto) {
    return digitalWeeklyRepository.findById(id)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("DigitalWeekly", id)))
        .map(existing -> existing.toBuilder()
            .pdfUrl(dto.getPdfUrl() != null ? dto.getPdfUrl() : existing.getPdfUrl())
            .frontPageImageUrl(dto.getFrontPageImageUrl() != null ? dto.getFrontPageImageUrl() : existing.getFrontPageImageUrl())
            .descripcion(dto.getDescripcion() != null ? dto.getDescripcion() : existing.getDescripcion())
            .isActive(dto.getIsActive() != null ? dto.getIsActive() : existing.getIsActive())
            .isPremium(dto.getIsPremium() != null ? dto.getIsPremium() : existing.getIsPremium())
            .url(dto.getUrl() != null ? dto.getUrl() : existing.getUrl())
            .build())
        .flatMap(digitalWeeklyRepository::save)
        .map(this::toResponse);
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
