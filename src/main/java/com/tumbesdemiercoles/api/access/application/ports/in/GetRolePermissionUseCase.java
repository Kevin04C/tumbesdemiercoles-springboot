package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.RolePermissionResponseDto;
import java.util.UUID;
import reactor.core.publisher.Flux;

public interface GetRolePermissionUseCase {
  Flux<RolePermissionResponseDto> getByRoleId(UUID roleId);
}
