package com.tumbesdemiercoles.api.access.domain.repository;

import com.tumbesdemiercoles.api.access.domain.model.Permission;
import com.tumbesdemiercoles.api.access.domain.model.UserPermission;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de persistencia de UserPermission en el dominio.
 */
public interface UserPermissionRepository {

  Mono<UserPermission> save(UserPermission userPermission);

  Flux<UserPermission> findByUserId(UUID userId);

  Mono<UserPermission> findByUserIdAndPermissionId(UUID userId, UUID permissionId);

  Mono<Boolean> existsByUserIdAndPermissionId(UUID userId, UUID permissionId);

  Mono<Void> deleteByUserIdAndPermissionId(UUID userId, UUID permissionId);

  Flux<Permission> findEffectivePermissionsByUserId(UUID userId);

}
