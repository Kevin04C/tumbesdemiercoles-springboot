package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.AssignUserRoleRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.UserRoleResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Tag(name = "User Role", description = "Endpoints for managing user roles")
public interface UserRoleControllerApi {

  @Operation(summary = "Assign role to user")
  @PostMapping
  Mono<ApiResponse<UserRoleResponse>> assignRole(
      @PathVariable UUID userId,
      @RequestBody AssignUserRoleRequest request);

  @Operation(summary = "Revoke role from user")
  @DeleteMapping("/{roleId}")
  Mono<ApiResponse<UserRoleResponse>> revokeRole(
      @PathVariable UUID userId,
      @PathVariable UUID roleId);

  @Operation(summary = "Get user roles")
  @GetMapping
  Mono<ApiResponse<List<UserRoleResponse>>> getUserRoles(
      @PathVariable UUID userId);
}
