package com.tumbesdemiercoles.api.access.infrastructure.repository;

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

}
