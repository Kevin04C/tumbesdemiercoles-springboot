package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.ports.in.DeletePermissionUseCase;
import com.tumbesdemiercoles.api.access.domain.exception.PermissionNotFoundException;
import com.tumbesdemiercoles.api.access.domain.repository.PermissionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeletePermissionUseCaseImpl implements DeletePermissionUseCase {

  private final PermissionRepository permissionRepository;

  @Override
  public Mono<Void> deletePermission(UUID permissionId) {
    return permissionRepository.findById(permissionId)
        .switchIfEmpty(Mono.error(new PermissionNotFoundException(permissionId)))
        .flatMap(permission -> {
          permission.deletePermission();
          return permissionRepository.save(permission);
        })
        .then();
  }

}
