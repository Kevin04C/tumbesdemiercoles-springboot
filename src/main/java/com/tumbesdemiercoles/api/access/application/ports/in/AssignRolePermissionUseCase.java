package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.AssignRolePermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.RolePermissionResponseDto;
import reactor.core.publisher.Flux;

public interface AssignRolePermissionUseCase {
  Flux<RolePermissionResponseDto> execute(AssignRolePermissionRequestDto assignRolePermissionRequestDto);
}
