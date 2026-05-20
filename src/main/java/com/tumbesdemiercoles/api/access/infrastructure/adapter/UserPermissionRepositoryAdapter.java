package com.tumbesdemiercoles.api.access.infrastructure.adapter;

import com.tumbesdemiercoles.api.access.domain.model.Permission;
import com.tumbesdemiercoles.api.access.domain.model.UserPermission;
import com.tumbesdemiercoles.api.access.domain.repository.UserPermissionRepository;
import com.tumbesdemiercoles.api.access.infrastructure.mapper.PermissionPersistenceMapper;
import com.tumbesdemiercoles.api.access.infrastructure.mapper.UserPermissionPersistenceMapper;
import com.tumbesdemiercoles.api.access.infrastructure.repository.UserPermissionR2dbcRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserPermissionRepositoryAdapter implements UserPermissionRepository {

  private final UserPermissionR2dbcRepository r2dbcRepository;
  private final UserPermissionPersistenceMapper mapper;
  private final PermissionPersistenceMapper permissionMapper;

  @Override
  public Mono<UserPermission> save(UserPermission userPermission) {
    return r2dbcRepository.save(userPermission.getId() == null ? mapper.toEntity(userPermission) : mapper.toEntityUpdate(userPermission))
        .map(mapper::toDomain);
  }

  @Override
  public Flux<UserPermission> findByUserId(UUID userId) {
    return r2dbcRepository.findByUserId(userId)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<UserPermission> findByUserIdAndPermissionId(UUID userId, UUID permissionId) {
    return r2dbcRepository.findByUserIdAndPermissionId(userId, permissionId)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Boolean> existsByUserIdAndPermissionId(UUID userId, UUID permissionId) {
    return r2dbcRepository.existsByUserIdAndPermissionId(userId, permissionId);
  }

  @Override
  public Mono<Void> deleteByUserIdAndPermissionId(UUID userId, UUID permissionId) {
    return r2dbcRepository.deleteByUserIdAndPermissionId(userId, permissionId);
  }

  @Override
  public Flux<Permission> findEffectivePermissionsByUserId(UUID userId) {
    return r2dbcRepository.findEffectivePermissionsByUserId(userId)
        .map(permissionMapper::toDomain);
  }

}
