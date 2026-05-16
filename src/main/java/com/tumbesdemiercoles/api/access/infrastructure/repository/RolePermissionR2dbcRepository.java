package com.tumbesdemiercoles.api.access.infrastructure.repository;

import com.tumbesdemiercoles.api.access.infrastructure.entity.RolePermissionEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RolePermissionR2dbcRepository extends ReactiveCrudRepository<RolePermissionEntity, UUID> {

  Flux<RolePermissionEntity> findByRoleId(UUID roleId);

  Mono<RolePermissionEntity> findByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

  Mono<Boolean> existsByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

}
