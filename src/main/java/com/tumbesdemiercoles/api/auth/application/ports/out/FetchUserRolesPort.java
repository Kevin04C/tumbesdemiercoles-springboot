package com.tumbesdemiercoles.api.auth.application.ports.out;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface FetchUserRolesPort {
    Mono<List<String>> getRolesForUser(UUID userId);
}
