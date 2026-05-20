package com.tumbesdemiercoles.api.access.infrastructure.adapter;

import com.tumbesdemiercoles.api.access.domain.model.RolePermission;
import com.tumbesdemiercoles.api.access.domain.repository.RolePermissionRepository;
import com.tumbesdemiercoles.api.access.infrastructure.mapper.RolePermissionPersistenceMapper;
import com.tumbesdemiercoles.api.access.infrastructure.repository.RolePermissionR2dbcRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RolePermissionRepositoryAdapter implements RolePermissionRepository {

  private final RolePermissionR2dbcRepository r2dbcRepository;
  private final RolePermissionPersistenceMapper mapper;

  @Override
  public Mono<RolePermission> save(RolePermission rolePermission) {
    return r2dbcRepository.save(rolePermission.getId() == null ? mapper.toEntity(rolePermission) : mapper.toEntityUpdate(rolePermission))
        .map(mapper::toDomain);
  }

  @Override
  public Flux<RolePermission> findByRoleId(UUID roleId) {
    return r2dbcRepository.findByRoleId(roleId)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<RolePermission> findByRoleIdAndPermissionId(UUID roleId, UUID permissionId) {
    return r2dbcRepository.findByRoleIdAndPermissionId(roleId, permissionId)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Boolean> existsByRoleIdAndPermissionId(UUID roleId, UUID permissionId) {
    return r2dbcRepository.existsByRoleIdAndPermissionId(roleId, permissionId);
  }

  @Override
  public Flux<RolePermission> saveAll(List<RolePermission> rolePermissions) {
    return Flux.fromIterable(rolePermissions)
        .map(domain -> domain.getId() == null
            ? mapper.toEntity(domain)
            : mapper.toEntityUpdate(domain))
        .collectList()
        .flatMapMany(r2dbcRepository::saveAll)
        .map(mapper::toDomain);
  }

}
