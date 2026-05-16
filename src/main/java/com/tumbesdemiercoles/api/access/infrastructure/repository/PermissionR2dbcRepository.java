package com.tumbesdemiercoles.api.access.infrastructure.repository;

import com.tumbesdemiercoles.api.access.infrastructure.entity.PermissionEntity;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PermissionR2dbcRepository extends ReactiveCrudRepository<PermissionEntity, UUID> {

  Mono<Boolean> existsByPermissionName(String permissionName);

  @Query("SELECT p.name FROM permission p " +
      "JOIN role_permission rp ON p.id = rp.permission_id " +
      "JOIN role r ON rp.role_id = r.id " +
      "WHERE r.name = :roleName AND rp.status_registry = 'ACTIVE'")
  Flux<String> findPermissionNamesByRoleName(String roleName);

}
