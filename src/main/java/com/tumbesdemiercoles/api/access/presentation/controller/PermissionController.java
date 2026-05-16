package com.tumbesdemiercoles.api.access.presentation.controller;

import com.tumbesdemiercoles.api.access.application.ports.in.CreatePermissionUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.DeletePermissionUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.GetPermissionUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.UpdatePermissionUseCase;
import com.tumbesdemiercoles.api.access.presentation.api.PermissionControllerApi;
import com.tumbesdemiercoles.api.access.presentation.dto.request.PermissionCreateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.request.PermissionUpdateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.PermissionResponse;
import com.tumbesdemiercoles.api.access.presentation.mapper.PermissionWebMapper;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PermissionController implements PermissionControllerApi {

  private final CreatePermissionUseCase createPermissionUseCase;
  private final GetPermissionUseCase getPermissionUseCase;
  private final UpdatePermissionUseCase updatePermissionUseCase;
  private final DeletePermissionUseCase deletePermissionUseCase;
  private final PermissionWebMapper permissionWebMapper;

  @Override
  public Mono<ApiResponse<List<PermissionResponse>>> findAllPermissions() {
    return getPermissionUseCase.findAll()
        .map(permissionWebMapper::toResponse)
        .collectList()
        .map(permissions -> ApiResponse.success(permissions, "Permisos encontrados"));
  }

  @Override
  public Mono<ApiResponse<PermissionResponse>> getPermissionById(UUID permissionId) {
    return getPermissionUseCase.getById(permissionId)
        .map(permissionWebMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Permiso encontrado exitosamente"));
  }

  @Override
  public Mono<ApiResponse<PermissionResponse>> createPermission(PermissionCreateRequest permissionCreateRequest) {
    return createPermissionUseCase.execute(permissionWebMapper.toDto(permissionCreateRequest))
        .map(permissionWebMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Permiso creado exitosamente"));
  }

  @Override
  public Mono<ApiResponse<PermissionResponse>> updatePermission(UUID permissionId, PermissionUpdateRequest permissionUpdateRequest) {
    return updatePermissionUseCase.updatePermission(permissionId, permissionWebMapper.toUpdateDto(permissionUpdateRequest))
        .map(permissionWebMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Permiso actualizado correctamente"));
  }

  @Override
  public Mono<ApiResponse<Void>> deletePermission(UUID permissionId) {
    return deletePermissionUseCase.deletePermission(permissionId)
        .thenReturn(ApiResponse.success(null, "Permiso eliminado correctamente"));
  }

}
