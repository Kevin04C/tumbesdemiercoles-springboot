package com.tumbesdemiercoles.api.access.presentation.controller;

import com.tumbesdemiercoles.api.access.application.dto.UpdateUserPermissionExceptionsRequestDto;
import com.tumbesdemiercoles.api.access.application.ports.in.GetEffectivePermissionsUseCase;
import com.tumbesdemiercoles.api.access.application.ports.in.ManageUserPermissionExceptionsUseCase;
import com.tumbesdemiercoles.api.access.presentation.api.UserPermissionControllerApi;
import com.tumbesdemiercoles.api.access.presentation.dto.request.UpdateUserPermissionExceptionsRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.PermissionResponse;
import com.tumbesdemiercoles.api.access.presentation.dto.response.UserPermissionResponse;
import com.tumbesdemiercoles.api.access.presentation.mapper.PermissionWebMapper;
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

  private final ManageUserPermissionExceptionsUseCase manageUserPermissionExceptionsUseCase;
  private final GetEffectivePermissionsUseCase getEffectivePermissionsUseCase;
  private final UserPermissionWebMapper userPermissionWebMapper;
  private final PermissionWebMapper permissionWebMapper;

  @Override
  public Mono<ApiResponse<List<UserPermissionResponse>>> updateExceptions(UUID userId, UpdateUserPermissionExceptionsRequest request) {
    UpdateUserPermissionExceptionsRequestDto requestDto = UpdateUserPermissionExceptionsRequestDto.builder()
        .overrides(request.getOverrides().stream()
            .map(o -> UpdateUserPermissionExceptionsRequestDto.PermissionOverrideDto.builder()
                .permissionId(o.getPermissionId())
                .isActive(o.getIsActive())
                .build())
            .toList())
        .build();

    return manageUserPermissionExceptionsUseCase.execute(userId, requestDto)
        .map(userPermissionWebMapper::toResponse)
        .collectList()
        .map(list -> ApiResponse.success(list, "Excepciones de permisos del usuario actualizadas exitosamente"));
  }

  @Override
  public Mono<ApiResponse<List<PermissionResponse>>> getEffectivePermissions(UUID userId) {
    return getEffectivePermissionsUseCase.execute(userId)
        .map(permissionWebMapper::toResponse)
        .collectList()
        .map(list -> ApiResponse.success(list, "Permisos efectivos del usuario encontrados"));
  }

}
