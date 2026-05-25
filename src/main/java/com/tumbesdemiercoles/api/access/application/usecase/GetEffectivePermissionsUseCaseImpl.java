package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.PermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.GetEffectivePermissionsUseCase;
import com.tumbesdemiercoles.api.access.application.ports.out.UserExistencePort;
import com.tumbesdemiercoles.api.access.domain.model.Permission;
import com.tumbesdemiercoles.api.access.domain.repository.UserPermissionRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetEffectivePermissionsUseCaseImpl implements GetEffectivePermissionsUseCase {

  private final UserExistencePort userExistencePort;
  private final UserPermissionRepository userPermissionRepository;

  @Override
  public Flux<PermissionResponseDto> execute(UUID userId) {
    return userExistencePort.existsById(userId)
        .filter(exists -> exists)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("User", userId)))
        .thenMany(Flux.defer(() -> userPermissionRepository.findEffectivePermissionsByUserId(userId)))
        .map(this::toResponse);
  }

  private PermissionResponseDto toResponse(Permission permission) {
    return PermissionResponseDto.builder()
        .id(permission.getId())
        .name(permission.getName())
        .description(permission.getDescription())
        .statusRegistry(permission.getStatusRegistry())
        .build();
  }

}
