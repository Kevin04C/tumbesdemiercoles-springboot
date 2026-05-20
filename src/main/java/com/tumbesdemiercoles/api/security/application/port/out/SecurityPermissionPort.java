package com.tumbesdemiercoles.api.security.application.port.out;

import java.util.UUID;
import reactor.core.publisher.Flux;

public interface SecurityPermissionPort {
  Flux<String> getPermissionsForUser(UUID userId);
}
