package com.tumbesdemiercoles.api.access.infrastructure.repository;

import com.tumbesdemiercoles.api.access.infrastructure.entity.RoleEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RoleR2dbcRepository extends ReactiveCrudRepository<RoleEntity, UUID> {

  Mono<Boolean> existsByRoleName(String roleName);

}
