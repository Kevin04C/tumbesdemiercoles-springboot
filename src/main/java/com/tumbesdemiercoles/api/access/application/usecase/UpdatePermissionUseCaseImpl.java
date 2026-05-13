package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.PermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.PermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.UpdatePermissionUseCase;
import com.tumbesdemiercoles.api.access.domain.exception.PermissionNotFoundException;
import com.tumbesdemiercoles.api.access.domain.model.Permission;
import com.tumbesdemiercoles.api.access.domain.repository.PermissionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdatePermissionUseCaseImpl implements UpdatePermissionUseCase {

  private final PermissionRepository permissionRepository;

  @Override
  public Mono<PermissionResponseDto> updatePermission(UUID permissionId, PermissionRequestDto permissionRequestDto) {
    return permissionRepository.findById(permissionId)
        .switchIfEmpty(Mono.error(new PermissionNotFoundException(permissionId)))
        .map(existingPermission -> {
          String newName = permissionRequestDto.getName() != null ? permissionRequestDto.getName() : existingPermission.getName();
          String newDescription = permissionRequestDto.getDescription() != null ? permissionRequestDto.getDescription() : existingPermission.getDescription();
          existingPermission.updatePermission(newName, newDescription);
          return existingPermission;
        })
        .flatMap(permissionRepository::save)
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
