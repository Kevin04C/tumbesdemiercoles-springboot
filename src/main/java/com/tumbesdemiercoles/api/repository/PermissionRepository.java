package com.tumbesdemiercoles.api.repository;

import com.tumbesdemiercoles.api.entities.Permission;
import com.tumbesdemiercoles.api.repository.custom.PermissionRepositoryCustom;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PermissionRepository extends ReactiveCrudRepository<Permission, UUID> , PermissionRepositoryCustom {

  /**
   * Extrae los nombres de los permisos cruzando las tablas Roles y Permisos.
   */
  @Query("SELECT p.PermissionName FROM dbo.Permissions p " +
      "JOIN dbo.RolePermissions rp ON p.PermissionID = rp.PermissionID " +
      "JOIN dbo.Roles r ON rp.RoleID = r.RoleID " +
      "WHERE r.Name = :roleName AND rp.StatusRegistry = 'ACTIVO'")
  Flux<String> findPermissionNamesByRoleName(String roleName);

  Mono<Boolean> existsByPermissionName(String permissionName);


}