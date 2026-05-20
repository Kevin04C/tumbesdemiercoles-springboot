package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.ports.in.RevokeUserPermissionUseCase;
import com.tumbesdemiercoles.api.access.domain.repository.UserPermissionRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RevokeUserPermissionUseCaseImpl implements RevokeUserPermissionUseCase {

  private final UserPermissionRepository userPermissionRepository;

  @Override
  @Transactional
  public Mono<Void> revoke(UUID userId, UUID permissionId) {
    return userPermissionRepository.findByUserIdAndPermissionId(userId, permissionId)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("UserPermission",
            "userId=" + userId + ", permissionId=" + permissionId)))
        .flatMap(userPermission -> {
          userPermission.revokePermission();
          return userPermissionRepository.save(userPermission);
        })
        .then();
  }

}
