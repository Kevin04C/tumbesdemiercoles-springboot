package com.tumbesdemiercoles.api.access.infrastructure.adapter;

import com.tumbesdemiercoles.api.access.domain.model.Permission;
import com.tumbesdemiercoles.api.access.domain.repository.PermissionRepository;
import com.tumbesdemiercoles.api.access.infrastructure.mapper.PermissionPersistenceMapper;
import com.tumbesdemiercoles.api.access.infrastructure.repository.PermissionR2dbcRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {

  private final PermissionR2dbcRepository r2dbcRepository;
  private final PermissionPersistenceMapper mapper;

  @Override
  public Mono<Permission> save(Permission permission) {
    return r2dbcRepository.save(permission.getId() == null ? mapper.toEntity(permission) : mapper.toEntityUpdate(permission))
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Permission> findById(UUID id) {
    return r2dbcRepository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Flux<Permission> findAll() {
    return r2dbcRepository.findAll()
        .map(mapper::toDomain);
  }

  @Override
  public Flux<Permission> findAllById(List<UUID> ids) {
    return r2dbcRepository.findAllById(ids)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Boolean> existsByName(String name) {
    return r2dbcRepository.existsByPermissionName(name);
  }

}
