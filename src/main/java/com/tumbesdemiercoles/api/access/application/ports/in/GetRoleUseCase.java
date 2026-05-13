package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.RoleResponseDto;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetRoleUseCase {
  Mono<RoleResponseDto> getById(UUID roleId);
  Flux<RoleResponseDto> findAll();
}
