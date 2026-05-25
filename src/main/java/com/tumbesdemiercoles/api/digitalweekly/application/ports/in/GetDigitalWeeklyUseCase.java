package com.tumbesdemiercoles.api.digitalweekly.application.ports.in;

import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyResponseDto;
import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeeklyFilter;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface GetDigitalWeeklyUseCase {
  Mono<DigitalWeeklyResponseDto> getById(UUID id);
  Mono<PageResponseDto<DigitalWeeklyResponseDto>> findDigitalWeeklies(DigitalWeeklyFilter filter);
}
