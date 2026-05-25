package com.tumbesdemiercoles.api.access.infrastructure.repository;

import com.tumbesdemiercoles.api.access.infrastructure.entity.RolePermissionEntity;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RolePermissionR2dbcRepository extends ReactiveCrudRepository<RolePermissionEntity, UUID> {

  Flux<RolePermissionEntity> findByRoleId(UUID roleId);

  Mono<RolePermissionEntity> findByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

  Mono<Boolean> existsByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

  @Modifying
  @Query("DELETE FROM role_permission WHERE role_id = :roleId AND permission_id = :permissionId")
  Mono<Void> deleteByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

}

