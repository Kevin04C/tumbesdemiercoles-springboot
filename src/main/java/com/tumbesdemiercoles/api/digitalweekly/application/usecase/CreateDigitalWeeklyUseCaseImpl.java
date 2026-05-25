package com.tumbesdemiercoles.api.digitalweekly.application.usecase;

import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyRequestDto;
import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyResponseDto;
import com.tumbesdemiercoles.api.digitalweekly.application.ports.in.CreateDigitalWeeklyUseCase;
import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeekly;
import com.tumbesdemiercoles.api.digitalweekly.domain.repository.DigitalWeeklyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateDigitalWeeklyUseCaseImpl implements CreateDigitalWeeklyUseCase {

  private final DigitalWeeklyRepository digitalWeeklyRepository;

  @Override
  public Mono<DigitalWeeklyResponseDto> execute(DigitalWeeklyRequestDto dto) {
    DigitalWeekly digitalWeekly = DigitalWeekly.builder()
        .pdfUrl(dto.getPdfUrl())
        .frontPageImageUrl(dto.getFrontPageImageUrl())
        .descripcion(dto.getDescripcion())
        .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
        .isPremium(dto.getIsPremium() != null ? dto.getIsPremium() : false)
        .url(dto.getUrl())
        .build();

    return digitalWeeklyRepository.save(digitalWeekly)
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
