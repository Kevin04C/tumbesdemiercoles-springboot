package com.tumbesdemiercoles.api.access.presentation.controller;

import com.tumbesdemiercoles.api.access.application.dto.AssignUserPermissionRequestDto;
import com.tumbesdemiercoles.api.access.application.ports.in.AssignUserPermissionUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.GetUserPermissionUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.RevokeUserPermissionUseCase;
import com.tumbesdemiercoles.api.access.presentation.api.UserPermissionControllerApi;
import com.tumbesdemiercoles.api.access.presentation.dto.request.AssignUserPermissionRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.UserPermissionResponse;
import com.tumbesdemiercoles.api.access.presentation.mapper.UserPermissionWebMapper;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserPermissionController implements UserPermissionControllerApi {

  private final AssignUserPermissionUseCase assignUserPermissionUseCase;
  private final GetUserPermissionUseCase getUserPermissionUseCase;
  private final RevokeUserPermissionUseCase revokeUserPermissionUseCase;
  private final UserPermissionWebMapper userPermissionWebMapper;

  @Override
  public Mono<ApiResponse<List<UserPermissionResponse>>> getPermissionsByUserId(UUID userId) {
    return getUserPermissionUseCase.getByUserId(userId)
        .map(userPermissionWebMapper::toResponse)
        .collectList()
        .map(list -> ApiResponse.success(list, "Permisos del usuario encontrados"));
  }

  @Override
  public Mono<ApiResponse<List<UserPermissionResponse>>> assignPermissions(UUID userId, AssignUserPermissionRequest assignUserPermissionRequest) {
    AssignUserPermissionRequestDto requestDto = AssignUserPermissionRequestDto.builder()
        .userId(userId)
        .permissionIds(assignUserPermissionRequest.getPermissionIds())
        .build();

    return assignUserPermissionUseCase.execute(requestDto)
        .map(userPermissionWebMapper::toResponse)
        .collectList()
        .map(list -> ApiResponse.success(list, "Permisos asignados al usuario exitosamente"));
  }

  @Override
  public Mono<ApiResponse<Void>> revokePermission(UUID userId, UUID permissionId) {
    return revokeUserPermissionUseCase.revoke(userId, permissionId)
        .thenReturn(ApiResponse.success(null, "Permiso revocado del usuario correctamente"));
  }

}
