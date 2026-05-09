package com.tumbesdemiercoles.api.user.infrastructure.repository;

import com.tumbesdemiercoles.api.shared.dto.UserAuthorityDto;
import com.tumbesdemiercoles.api.user.infrastructure.entity.UserEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio de Spring Data R2DBC para la entidad UserEntity.
 * Solo existe en la capa de infraestructura.
 */
public interface UserR2dbcRepository extends ReactiveCrudRepository<UserEntity, UUID> {

  Mono<UserEntity> findByUserEmail(String email);

  Flux<UserAuthorityDto> findAuthoritiesById(UUID id);

}
