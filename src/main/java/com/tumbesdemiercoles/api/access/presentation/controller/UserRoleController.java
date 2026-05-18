package com.tumbesdemiercoles.api.access.presentation.controller;

import com.tumbesdemiercoles.api.access.application.usecase.AssignUserRoleUseCase;
import com.tumbesdemiercoles.api.access.application.usecase.FindUserRoleUseCase;
import com.tumbesdemiercoles.api.access.application.usecase.RevokeUserRoleUseCase;
import com.tumbesdemiercoles.api.access.presentation.api.UserRoleControllerApi;
import com.tumbesdemiercoles.api.access.presentation.dto.request.AssignUserRoleRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.UserRoleResponse;
import com.tumbesdemiercoles.api.access.presentation.mapper.UserRoleWebMapper;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users/{userId}/roles")
@RequiredArgsConstructor
public class UserRoleController implements UserRoleControllerApi {

  private final AssignUserRoleUseCase assignUserRoleUseCase;
  private final RevokeUserRoleUseCase revokeUserRoleUseCase;
  private final FindUserRoleUseCase findUserRoleUseCase;
  private final UserRoleWebMapper mapper;

  @Override
  public Mono<ResponseEntity<UserRoleResponse>> assignRole(
      UUID userId,
      @Valid AssignUserRoleRequest request) {
    return assignUserRoleUseCase.execute(userId, request.roleId())
        .map(mapper::toResponse)
        .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
  }

  @Override
  public Mono<ResponseEntity<UserRoleResponse>> revokeRole(
      UUID userId,
      UUID roleId) {
    return revokeUserRoleUseCase.execute(userId, roleId)
        .map(mapper::toResponse)
        .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<UserRoleResponse>>> getUserRoles(UUID userId) {
    Flux<UserRoleResponse> roles = findUserRoleUseCase.findByUserId(userId)
        .map(mapper::toResponse);
    return Mono.just(ResponseEntity.ok(roles));
  }
}
