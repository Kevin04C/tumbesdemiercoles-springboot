package com.tumbesdemiercoles.api.access.infrastructure.repository;

import com.tumbesdemiercoles.api.access.application.dto.UserRoleWithNameDto;
import com.tumbesdemiercoles.api.access.infrastructure.entity.UserRoleEntity;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRoleR2dbcRepository extends ReactiveCrudRepository<UserRoleEntity, UUID> {

  @Query("SELECT * FROM user_role WHERE user_id = :userId")
  Flux<UserRoleEntity> findByUserId(UUID userId);

  @Query("SELECT * FROM user_role WHERE user_id = :userId AND role_id = :roleId LIMIT 1")
  Mono<UserRoleEntity> findByUserIdAndRoleId(UUID userId, UUID roleId);

  @Query("SELECT r.name " +
          "FROM role r " +
          "INNER JOIN user_role ur ON r.id = ur.role_id " +
          "WHERE ur.user_id = :userId " +
          "AND ur.status_registry = 'ACTIVE' " +
          "AND r.status_registry = 'ACTIVE'")
  Flux<String> findRoleNamesByUserId(UUID userId);

  @Query("SELECT ur.*, r.name AS role_name " +
          "FROM user_role ur " +
          "INNER JOIN role r ON ur.role_id = r.id " +
          "WHERE ur.user_id = :userId " +
          "AND ur.status_registry = 'ACTIVE' " +
          "AND r.status_registry = 'ACTIVE'")
  Flux<UserRoleWithNameDto> findActiveRolesWithNamesByUserId(UUID userId);

  @Query("SELECT * FROM user_role WHERE role_id = :roleId AND status_registry = 'ACTIVE'")
  Flux<UserRoleEntity> findActiveByRoleId(UUID roleId);

}

