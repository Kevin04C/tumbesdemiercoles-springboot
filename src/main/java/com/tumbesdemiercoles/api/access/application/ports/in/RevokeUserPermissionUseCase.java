package com.tumbesdemiercoles.api.access.application.ports.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface RevokeUserPermissionUseCase {
  Mono<Void> revoke(UUID userId, UUID permissionId);
}
