package com.tumbesdemiercoles.api.access.infrastructure.repository;

import com.tumbesdemiercoles.api.access.infrastructure.entity.PermissionEntity;
import com.tumbesdemiercoles.api.access.infrastructure.entity.UserPermissionEntity;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPermissionR2dbcRepository extends ReactiveCrudRepository<UserPermissionEntity, UUID> {

  Flux<UserPermissionEntity> findByUserId(UUID userId);

  Mono<UserPermissionEntity> findByUserIdAndPermissionId(UUID userId, UUID permissionId);

  Mono<Boolean> existsByUserIdAndPermissionId(UUID userId, UUID permissionId);

  @Modifying
  @Query("DELETE FROM user_permission WHERE user_id = :userId AND permission_id = :permissionId")
  Mono<Void> deleteByUserIdAndPermissionId(UUID userId, UUID permissionId);

  @Query("SELECT p.* FROM permission p " +
      "WHERE p.status_registry = 'ACTIVE' AND (" +
      "  (p.id IN (" +
      "      SELECT rp.permission_id FROM role_permission rp " +
      "      JOIN user_role ur ON ur.role_id = rp.role_id " +
      "      WHERE ur.user_id = :userId AND ur.status_registry = 'ACTIVE' AND rp.status_registry = 'ACTIVE'" +
      "   ) AND NOT EXISTS (" +
      "      SELECT 1 FROM user_permission up " +
      "      WHERE up.user_id = :userId AND up.permission_id = p.id AND up.is_active = FALSE AND up.status_registry = 'ACTIVE'" +
      "   ))" +
      "   OR EXISTS (" +
      "      SELECT 1 FROM user_permission up " +
      "      WHERE up.user_id = :userId AND up.permission_id = p.id AND up.is_active = TRUE AND up.status_registry = 'ACTIVE'" +
      "   )" +
      ")")
  Flux<PermissionEntity> findEffectivePermissionsByUserId(UUID userId);

}
