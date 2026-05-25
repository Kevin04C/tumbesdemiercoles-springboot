package com.tumbesdemiercoles.api.access.presentation.controller;

import com.tumbesdemiercoles.api.access.application.dto.AssignRolePermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.ports.in.AssignRolePermissionUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.GetRolePermissionUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.RevokeRolePermissionUseCase;
import com.tumbesdemiercoles.api.access.presentation.api.RolePermissionControllerApi;
import com.tumbesdemiercoles.api.access.presentation.dto.request.AssignRolePermissionRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.RolePermissionResponse;
import com.tumbesdemiercoles.api.access.presentation.mapper.RolePermissionWebMapper;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RolePermissionController implements RolePermissionControllerApi {

  private final AssignRolePermissionUseCase assignRolePermissionUseCase;
  private final GetRolePermissionUseCase getRolePermissionUseCase;
  private final RevokeRolePermissionUseCase revokeRolePermissionUseCase;
  private final RolePermissionWebMapper rolePermissionWebMapper;

  @Override
  public Mono<ApiResponse<List<RolePermissionResponse>>> getPermissionsByRoleId(UUID roleId) {
    return getRolePermissionUseCase.getByRoleId(roleId)
        .map(rolePermissionWebMapper::toResponse)
        .collectList()
        .map(list -> ApiResponse.success(list, "Permisos del rol encontrados"));
  }

  @Override
  public Mono<ApiResponse<List<RolePermissionResponse>>> assignPermissions(UUID roleId, AssignRolePermissionRequest assignRolePermissionRequest) {

    return Mono.fromSupplier(() -> rolePermissionWebMapper.toDto(roleId, assignRolePermissionRequest))
        .flatMapMany(assignRolePermissionUseCase::execute)
        .map(rolePermissionWebMapper::toResponse)
        .collectList()
        .map(list -> ApiResponse.success(list, "Permisos asignados al rol exitosamente"));
  }

  @Override
  public Mono<ApiResponse<Void>> revokePermission(UUID roleId, UUID permissionId) {
    return revokeRolePermissionUseCase.revoke(roleId, permissionId)
        .thenReturn(ApiResponse.success((Void) null, "Permiso revocado del rol correctamente"));
  }

}
