package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.PermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.PermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.CreatePermissionUseCase;
import com.tumbesdemiercoles.api.access.domain.model.Permission;
import com.tumbesdemiercoles.api.access.domain.repository.PermissionRepository;
import com.tumbesdemiercoles.api.shared.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreatePermissionUseCaseImpl implements CreatePermissionUseCase {

  private final PermissionRepository permissionRepository;

  @Override
  public Mono<PermissionResponseDto> execute(PermissionRequestDto permissionRequestDto) {
    return permissionRepository.existsByName(permissionRequestDto.getName())
        .filter(exists -> !exists)
        .switchIfEmpty(Mono.error(() -> ConflictException.forDuplicate("Permission", "name", permissionRequestDto.getName())))
        .flatMap(available -> {
          Permission permission = Permission.createNewPermission(
              permissionRequestDto.getName(),
              permissionRequestDto.getDescription()
          );
          return permissionRepository.save(permission);
        })
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
