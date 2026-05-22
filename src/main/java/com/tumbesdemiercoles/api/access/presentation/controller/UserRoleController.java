package com.tumbesdemiercoles.api.access.presentation.controller;

import com.tumbesdemiercoles.api.access.application.usecase.AssignUserRoleUseCase;
import com.tumbesdemiercoles.api.access.application.usecase.FindUserRoleUseCaseImpl;
import com.tumbesdemiercoles.api.access.application.usecase.RevokeUserRoleUseCase;
import com.tumbesdemiercoles.api.access.presentation.api.UserRoleControllerApi;
import com.tumbesdemiercoles.api.access.presentation.dto.request.AssignUserRoleRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.UserRoleResponse;
import com.tumbesdemiercoles.api.access.presentation.mapper.UserRoleWebMapper;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users/{userId}/roles")
@RequiredArgsConstructor
public class UserRoleController implements UserRoleControllerApi {

  private final AssignUserRoleUseCase assignUserRoleUseCase;
  private final RevokeUserRoleUseCase revokeUserRoleUseCase;
  private final FindUserRoleUseCaseImpl findUserRoleUseCaseImpl;
  private final UserRoleWebMapper mapper;

  @Override
  public Mono<ApiResponse<UserRoleResponse>> assignRole(
      UUID userId,
      @Valid AssignUserRoleRequest request) {
    return assignUserRoleUseCase.execute(userId, request.roleId())
        .map(mapper::toResponse)
        .map(response -> ApiResponse.success(response, "Rol asignado correctamente"));
  }

  @Override
  public Mono<ApiResponse<UserRoleResponse>> revokeRole(
      UUID userId,
      UUID roleId) {
    return revokeUserRoleUseCase.execute(userId, roleId)
        .map(mapper::toResponse)
        .map(response -> ApiResponse.success(response, "Rol revocado correctamente"));
  }

  @Override
  public Mono<ApiResponse<List<UserRoleResponse>>> getUserRoles(UUID userId) {
    return findUserRoleUseCaseImpl.findByUserId(userId)
        .map(mapper::toResponse)
        .collectList()
        .map(roles -> ApiResponse.success(roles, "Roles de usuario encontrados"));
  }
}
