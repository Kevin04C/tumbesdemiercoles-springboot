package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.AssignRolePermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.RolePermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.AssignRolePermissionUseCase;
import com.tumbesdemiercoles.api.access.domain.exception.RoleNotFoundException;
import com.tumbesdemiercoles.api.access.domain.model.RolePermission;
import com.tumbesdemiercoles.api.access.domain.repository.PermissionRepository;
import com.tumbesdemiercoles.api.access.domain.repository.RolePermissionRepository;
import com.tumbesdemiercoles.api.access.domain.repository.RoleRepository;
import com.tumbesdemiercoles.api.access.domain.exception.PermissionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AssignRolePermissionUseCaseImpl implements AssignRolePermissionUseCase {

  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final RolePermissionRepository rolePermissionRepository;

  @Override
  public Flux<RolePermissionResponseDto> execute(AssignRolePermissionRequestDto assignRolePermissionRequestDto) {
    return roleRepository.findById(assignRolePermissionRequestDto.getRoleId())
        .switchIfEmpty(Mono.error(new RoleNotFoundException(assignRolePermissionRequestDto.getRoleId())))
        .thenMany(Flux.fromIterable(assignRolePermissionRequestDto.getPermissionIds())
            .flatMap(permissionId -> permissionRepository.findById(permissionId)
                .switchIfEmpty(Mono.error(new PermissionNotFoundException(permissionId)))
                .then(rolePermissionRepository.existsByRoleIdAndPermissionId(
                    assignRolePermissionRequestDto.getRoleId(), permissionId))
                .filter(exists -> !exists)
                .flatMap(available -> {
                  RolePermission rolePermission = RolePermission.assignPermission(
                      assignRolePermissionRequestDto.getRoleId(), permissionId);
                  return rolePermissionRepository.save(rolePermission);
                })
                .map(this::toResponse)
            ));
  }

  private RolePermissionResponseDto toResponse(RolePermission rolePermission) {
    return RolePermissionResponseDto.builder()
        .id(rolePermission.getId())
        .roleId(rolePermission.getRoleId())
        .permissionId(rolePermission.getPermissionId())
        .statusRegistry(rolePermission.getStatusRegistry())
        .build();
  }

}
