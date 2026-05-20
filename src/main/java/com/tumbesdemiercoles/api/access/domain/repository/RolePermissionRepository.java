package com.tumbesdemiercoles.api.access.domain.repository;

import com.tumbesdemiercoles.api.access.domain.model.RolePermission;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de persistencia de RolePermission en el dominio.
 */
public interface RolePermissionRepository {

  Mono<RolePermission> save(RolePermission rolePermission);

  Flux<RolePermission> findByRoleId(UUID roleId);

  Mono<RolePermission> findByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

  Mono<Boolean> existsByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

  Flux<RolePermission> saveAll(List<RolePermission> rolePermissions);

}
