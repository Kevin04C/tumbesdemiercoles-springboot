package com.tumbesdemiercoles.api.access.domain.repository;

import com.tumbesdemiercoles.api.access.domain.model.Role;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de persistencia de Role en el dominio.
 */
public interface RoleRepository {

  Mono<Role> save(Role role);

  Mono<Role> findById(UUID id);

  Flux<Role> findAll();

  Mono<Boolean> existsByName(String name);

}
