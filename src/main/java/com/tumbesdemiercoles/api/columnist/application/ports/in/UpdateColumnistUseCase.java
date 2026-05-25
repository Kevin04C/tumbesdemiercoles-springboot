package com.tumbesdemiercoles.api.columnist.application.ports.in;

import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistRequestDto;
import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistResponseDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdateColumnistUseCase {
    Mono<ColumnistResponseDto> execute(UUID id, ColumnistRequestDto request);
}
