package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.PermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.PermissionResponseDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdatePermissionUseCase {
  Mono<PermissionResponseDto> updatePermission(UUID permissionId, PermissionRequestDto permissionRequestDto);
}
