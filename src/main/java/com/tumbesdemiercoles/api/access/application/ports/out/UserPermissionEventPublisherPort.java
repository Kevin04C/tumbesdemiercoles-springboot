package com.tumbesdemiercoles.api.access.application.ports.out;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserPermissionEventPublisherPort {
    Mono<Void> publishPermissionsChanged(UUID userId);
}
