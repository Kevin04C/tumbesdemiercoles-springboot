package com.tumbesdemiercoles.api.auth.application.ports.out;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UserRolAccessPort {
  Mono<Void> assignRole(UUID userId, String roleName);
}
