package com.tumbesdemiercoles.api.access.infrastructure.adapter;

import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import com.tumbesdemiercoles.api.access.domain.repository.UserRoleRepository;
import com.tumbesdemiercoles.api.access.infrastructure.mapper.UserRoleInfraMapper;
import com.tumbesdemiercoles.api.access.infrastructure.repository.UserRoleR2dbcRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRoleRepositoryAdapter implements UserRoleRepository {

  private final UserRoleR2dbcRepository repository;
  private final UserRoleInfraMapper mapper;

  @Override
  public Mono<UserRole> save(UserRole userRole) {
    return repository.save(mapper.toEntity(userRole))
        .map(mapper::toDomain);
  }

  @Override
  public Mono<UserRole> findByUserIdAndRoleId(UUID userId, UUID roleId) {
    return repository.findByUserIdAndRoleId(userId, roleId)
        .map(mapper::toDomain);
  }

  @Override
  public Flux<UserRole> findByUserId(UUID userId) {
    return repository.findActiveRolesWithNamesByUserId(userId)
        .map(mapper::toDomain);
  }

  @Override
  public Flux<String> findRoleNamesByUserId(UUID userId) {
    return repository.findRoleNamesByUserId(userId);
  }

}
