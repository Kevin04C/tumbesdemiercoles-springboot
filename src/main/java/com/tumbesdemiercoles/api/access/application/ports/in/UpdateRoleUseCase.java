package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.RoleRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.RoleResponseDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdateRoleUseCase {
  Mono<RoleResponseDto> updateRole(UUID roleId, RoleRequestDto roleRequestDto);
}
