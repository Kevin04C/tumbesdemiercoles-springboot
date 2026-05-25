package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.AssignUserRoleRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.UserRoleResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Tag(name = "User Role", description = "Endpoints para gestionar la asignación y revocación de roles a usuarios")
public interface UserRoleControllerApi {

  @Operation(summary = "Asignar rol a usuario", description = "Asocia un rol a un usuario determinado. Si el rol estaba eliminado lógicamente, se reactivará.")
  @PostMapping
  Mono<ApiResponse<UserRoleResponse>> assignRole(
      @Parameter(description = "ID del usuario", required = true) @PathVariable UUID userId,
      @RequestBody AssignUserRoleRequest request);

  @Operation(summary = "Revocar rol de usuario", description = "Revoca un rol previamente asignado a un usuario (eliminación lógica cambiando el estado a DELETE).")
  @DeleteMapping("/{roleId}")
  Mono<ApiResponse<UserRoleResponse>> revokeRole(
      @Parameter(description = "ID del usuario", required = true) @PathVariable UUID userId,
      @Parameter(description = "ID del rol a revocar", required = true) @PathVariable UUID roleId);

  @Operation(summary = "Listar roles de un usuario", description = "Retorna la lista de todos los roles asociados a un usuario específico.")
  @GetMapping
  Mono<ApiResponse<List<UserRoleResponse>>> getUserRoles(
      @Parameter(description = "ID del usuario a consultar", required = true) @PathVariable UUID userId);
}

