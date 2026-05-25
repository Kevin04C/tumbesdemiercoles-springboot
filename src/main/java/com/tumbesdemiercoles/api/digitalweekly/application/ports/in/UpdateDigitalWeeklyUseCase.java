package com.tumbesdemiercoles.api.digitalweekly.application.ports.in;

import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyRequestDto;
import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyResponseDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdateDigitalWeeklyUseCase {
  Mono<DigitalWeeklyResponseDto> execute(UUID id, DigitalWeeklyRequestDto dto);
}
