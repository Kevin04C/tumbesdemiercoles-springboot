package com.tumbesdemiercoles.api.access.infrastructure.repository;

import com.tumbesdemiercoles.api.access.infrastructure.entity.UserPermissionEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPermissionR2dbcRepository extends ReactiveCrudRepository<UserPermissionEntity, UUID> {

  Flux<UserPermissionEntity> findByUserId(UUID userId);

  Mono<UserPermissionEntity> findByUserIdAndPermissionId(UUID userId, UUID permissionId);

  Mono<Boolean> existsByUserIdAndPermissionId(UUID userId, UUID permissionId);

}
