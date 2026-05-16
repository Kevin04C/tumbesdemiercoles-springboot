package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.PermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.PermissionResponseDto;
import reactor.core.publisher.Mono;

public interface CreatePermissionUseCase {
  Mono<PermissionResponseDto> execute(PermissionRequestDto permissionRequestDto);
}
