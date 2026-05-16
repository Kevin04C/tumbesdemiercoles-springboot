package com.tumbesdemiercoles.api.access.presentation.controller;

import com.tumbesdemiercoles.api.access.application.ports.in.CreateRoleUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.DeleteRoleUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.GetRoleUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.UpdateRoleUseCase;
import com.tumbesdemiercoles.api.access.presentation.api.RoleControllerApi;
import com.tumbesdemiercoles.api.access.presentation.dto.request.RoleCreateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.request.RoleUpdateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.RoleResponse;
import com.tumbesdemiercoles.api.access.presentation.mapper.RoleWebMapper;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RoleController implements RoleControllerApi {

  private final CreateRoleUseCase createRoleUseCase;
  private final GetRoleUseCase getRoleUseCase;
  private final UpdateRoleUseCase updateRoleUseCase;
  private final DeleteRoleUseCase deleteRoleUseCase;
  private final RoleWebMapper roleWebMapper;

  @Override
  public Mono<ApiResponse<List<RoleResponse>>> findAllRoles() {
    return getRoleUseCase.findAll()
        .map(roleWebMapper::toResponse)
        .collectList()
        .map(roles -> ApiResponse.success(roles, "Roles encontrados"));
  }

  @Override
  public Mono<ApiResponse<RoleResponse>> getRoleById(UUID roleId) {
    return getRoleUseCase.getById(roleId)
        .map(roleWebMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Rol encontrado exitosamente"));
  }

  @Override
  public Mono<ApiResponse<RoleResponse>> createRole(RoleCreateRequest roleCreateRequest) {
    return createRoleUseCase.execute(roleWebMapper.toDto(roleCreateRequest))
        .map(roleWebMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Rol creado exitosamente"));
  }

  @Override
  public Mono<ApiResponse<RoleResponse>> updateRole(UUID roleId, RoleUpdateRequest roleUpdateRequest) {
    return updateRoleUseCase.updateRole(roleId, roleWebMapper.toUpdateDto(roleUpdateRequest))
        .map(roleWebMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Rol actualizado correctamente"));
  }

  @Override
  public Mono<ApiResponse<Void>> deleteRole(UUID roleId) {
    return deleteRoleUseCase.deleteRole(roleId)
        .thenReturn(ApiResponse.success(null, "Rol eliminado correctamente"));
  }

}
