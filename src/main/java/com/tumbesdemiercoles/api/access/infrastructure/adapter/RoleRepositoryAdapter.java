package com.tumbesdemiercoles.api.access.infrastructure.adapter;

import com.tumbesdemiercoles.api.access.domain.model.Role;
import com.tumbesdemiercoles.api.access.domain.repository.RoleRepository;
import com.tumbesdemiercoles.api.access.infrastructure.mapper.RolePersistenceMapper;
import com.tumbesdemiercoles.api.access.infrastructure.repository.RoleR2dbcRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

  private final RoleR2dbcRepository r2dbcRepository;
  private final RolePersistenceMapper mapper;

  @Override
  public Mono<Role> save(Role role) {
    return r2dbcRepository.save(role.getId() == null ? mapper.toEntity(role) : mapper.toEntityUpdate(role))
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Role> findById(UUID id) {
    return r2dbcRepository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Flux<Role> findAll() {
    return r2dbcRepository.findAll()
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Boolean> existsByName(String name) {
    return r2dbcRepository.existsByRoleName(name);
  }

}
