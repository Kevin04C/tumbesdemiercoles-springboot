package com.tumbesdemiercoles.api.permission.infrastructure.repository;

import com.tumbesdemiercoles.api.permission.infrastructure.entity.PermissionEntity;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PermissionR2dbcRepository extends ReactiveCrudRepository<PermissionEntity, UUID>, PermissionRepositoryCustom {

  @Query("SELECT p.PermissionName FROM dbo.Permissions p " +
      "JOIN dbo.RolePermissions rp ON p.PermissionID = rp.PermissionID " +
      "JOIN dbo.Roles r ON rp.RoleID = r.RoleID " +
      "WHERE r.Name = :roleName AND rp.StatusRegistry = 'ACTIVO'")
  Flux<String> findPermissionNamesByRoleName(String roleName);

  Mono<Boolean> existsByPermissionName(String permissionName);

}
