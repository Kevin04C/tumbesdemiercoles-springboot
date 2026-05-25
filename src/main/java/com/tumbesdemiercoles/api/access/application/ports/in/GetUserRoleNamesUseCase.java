package com.tumbesdemiercoles.api.access.application.ports.in;

import reactor.core.publisher.Flux;

import java.util.UUID;

public interface GetUserRoleNamesUseCase {
    Flux<String> execute(UUID userId);
}
