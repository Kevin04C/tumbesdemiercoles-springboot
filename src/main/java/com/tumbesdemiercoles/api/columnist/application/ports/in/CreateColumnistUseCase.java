package com.tumbesdemiercoles.api.columnist.application.ports.in;

import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistRequestDto;
import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistResponseDto;
import reactor.core.publisher.Mono;

public interface CreateColumnistUseCase {
    Mono<ColumnistResponseDto> execute(ColumnistRequestDto request);
}
