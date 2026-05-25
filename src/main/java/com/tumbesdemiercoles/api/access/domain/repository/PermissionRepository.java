package com.tumbesdemiercoles.api.access.domain.repository;

import com.tumbesdemiercoles.api.access.domain.model.Permission;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de persistencia de Permission en el dominio.
 */
public interface PermissionRepository {

  Mono<Permission> save(Permission permission);

  Mono<Permission> findById(UUID id);

  Flux<Permission> findAll();

  Flux<Permission> findAllById(List<UUID> ids);

  Mono<Boolean> existsByName(String name);

}
