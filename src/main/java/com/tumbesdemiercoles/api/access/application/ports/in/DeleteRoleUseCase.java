package com.tumbesdemiercoles.api.access.application.ports.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteRoleUseCase {
  Mono<Void> deleteRole(UUID roleId);
}
