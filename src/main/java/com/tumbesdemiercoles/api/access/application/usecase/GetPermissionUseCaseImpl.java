package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.PermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.GetPermissionUseCase;
import com.tumbesdemiercoles.api.access.domain.exception.PermissionNotFoundException;
import com.tumbesdemiercoles.api.access.domain.model.Permission;
import com.tumbesdemiercoles.api.access.domain.repository.PermissionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetPermissionUseCaseImpl implements GetPermissionUseCase {

  private final PermissionRepository permissionRepository;

  @Override
  public Mono<PermissionResponseDto> getById(UUID permissionId) {
    return permissionRepository.findById(permissionId)
        .switchIfEmpty(Mono.error(new PermissionNotFoundException(permissionId)))
        .map(this::toResponse);
  }

  @Override
  public Flux<PermissionResponseDto> findAll() {
    return permissionRepository.findAll()
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
