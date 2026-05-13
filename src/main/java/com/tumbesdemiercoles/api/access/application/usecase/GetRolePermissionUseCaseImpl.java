package com.tumbesdemiercoles.api.access.application.usecase;

import com.tumbesdemiercoles.api.access.application.dto.RolePermissionResponseDto;
import com.tumbesdemiercoles.api.access.application.ports.in.GetRolePermissionUseCase;
import com.tumbesdemiercoles.api.access.domain.model.RolePermission;
import com.tumbesdemiercoles.api.access.domain.repository.RolePermissionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetRolePermissionUseCaseImpl implements GetRolePermissionUseCase {

  private final RolePermissionRepository rolePermissionRepository;

  @Override
  public Flux<RolePermissionResponseDto> getByRoleId(UUID roleId) {
    return rolePermissionRepository.findByRoleId(roleId)
        .map(this::toResponse);
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
