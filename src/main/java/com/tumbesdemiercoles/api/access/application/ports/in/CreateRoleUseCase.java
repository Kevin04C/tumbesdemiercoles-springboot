package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.RoleRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.RoleResponseDto;
import reactor.core.publisher.Mono;

public interface CreateRoleUseCase {
  Mono<RoleResponseDto> execute(RoleRequestDto roleRequestDto);
}
