package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.PermissionResponseDto;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetPermissionUseCase {
  Mono<PermissionResponseDto> getById(UUID permissionId);
  Flux<PermissionResponseDto> findAll();
}
