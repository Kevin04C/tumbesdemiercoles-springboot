package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.ports.in.RevokeRolePermissionUseCase;
import com.tumbesdemiercoles.api.access.domain.repository.RolePermissionRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RevokeRolePermissionUseCaseImpl implements RevokeRolePermissionUseCase {

  private final RolePermissionRepository rolePermissionRepository;

  @Override
  @Transactional
  public Mono<Void> revoke(UUID roleId, UUID permissionId) {
    return rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("RolePermission",
            "roleId=" + roleId + ", permissionId=" + permissionId)))
        .flatMap(rolePermission -> {
          rolePermission.revokePermission();
          return rolePermissionRepository.save(rolePermission);
        })
        .then();
  }

}
