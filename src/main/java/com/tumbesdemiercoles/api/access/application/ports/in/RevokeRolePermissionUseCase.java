package com.tumbesdemiercoles.api.access.application.ports.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface RevokeRolePermissionUseCase {
  Mono<Void> revoke(UUID roleId, UUID permissionId);
}
