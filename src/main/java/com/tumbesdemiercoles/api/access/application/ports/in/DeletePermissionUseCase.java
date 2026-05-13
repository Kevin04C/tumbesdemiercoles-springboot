package com.tumbesdemiercoles.api.access.application.ports.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeletePermissionUseCase {
  Mono<Void> deletePermission(UUID permissionId);
}
