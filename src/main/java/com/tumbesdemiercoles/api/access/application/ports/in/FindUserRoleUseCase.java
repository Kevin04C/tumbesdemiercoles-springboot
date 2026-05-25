package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface FindUserRoleUseCase {

    Flux<UserRole> findByUserId(UUID userId);
}
