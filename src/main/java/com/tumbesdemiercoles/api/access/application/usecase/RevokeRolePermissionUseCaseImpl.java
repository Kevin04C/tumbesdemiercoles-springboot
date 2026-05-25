package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.ports.in.RevokeRolePermissionUseCase;
import com.tumbesdemiercoles.api.access.application.ports.out.UserPermissionEventPublisherPort;
import com.tumbesdemiercoles.api.access.domain.repository.RolePermissionRepository;
import com.tumbesdemiercoles.api.access.domain.repository.UserRoleRepository;
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
  private final UserRoleRepository userRoleRepository;
  private final UserPermissionEventPublisherPort userPermissionEventPublisherPort;

  @Override
  @Transactional
  public Mono<Void> revoke(UUID roleId, UUID permissionId) {
    return rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)
        .flatMap(exists -> {
          if (!exists) {
            return Mono.error(ResourceNotFoundException.forEntity("RolePermission",
                "roleId=" + roleId + ", permissionId=" + permissionId));
          }
          return rolePermissionRepository.deleteByRoleIdAndPermissionId(roleId, permissionId);
        })
        .then(Mono.defer(() -> invalidateUsersCacheForRole(roleId)));
  }


  private Mono<Void> invalidateUsersCacheForRole(UUID roleId) {
    return userRoleRepository.findActiveByRoleId(roleId)
        .flatMap(ur -> userPermissionEventPublisherPort.publishPermissionsChanged(ur.getUserId()))
        .then();
  }

}

