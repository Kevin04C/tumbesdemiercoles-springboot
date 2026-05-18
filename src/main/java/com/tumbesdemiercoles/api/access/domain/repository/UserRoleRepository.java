package com.tumbesdemiercoles.api.access.domain.repository;

import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRoleRepository {

  Mono<UserRole> save(UserRole userRole);

  Mono<UserRole> findByUserIdAndRoleId(UUID userId, UUID roleId);

  Flux<UserRole> findByUserId(UUID userId);

}
