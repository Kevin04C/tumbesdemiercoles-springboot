package com.tumbesdemiercoles.api.columnist.application.ports.in;

import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistResponseDto;
import com.tumbesdemiercoles.api.columnist.domain.model.ColumnistFilter;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface GetColumnistUseCase {
    Mono<ColumnistResponseDto> getById(UUID id);
    Mono<PageResponseDto<ColumnistResponseDto>> findColumnists(ColumnistFilter filter);
}
