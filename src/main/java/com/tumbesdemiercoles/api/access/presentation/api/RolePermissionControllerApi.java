package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.AssignRolePermissionRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.RolePermissionResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Role Permission", description = "Endpoints para gestionar las asociaciones de permisos que tiene cada rol")
@RequestMapping("/api/v1/roles/{roleId}/permissions")
public interface RolePermissionControllerApi {

  @Operation(summary = "Obtener permisos de un rol", description = "Retorna la lista de relaciones de permisos activas para un rol específico.")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<RolePermissionResponse>>> getPermissionsByRoleId(
      @Parameter(description = "ID del rol", required = true) @PathVariable UUID roleId);

  @Operation(summary = "Asignar permisos a un rol", description = "Asocia uno o más permisos a un rol en particular. Si el permiso ya estaba asociado, se ignora o reactiva.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<List<RolePermissionResponse>>> assignPermissions(
      @Parameter(description = "ID del rol", required = true) @PathVariable UUID roleId,
      @Valid @RequestBody AssignRolePermissionRequest assignRolePermissionRequest);

  @Operation(summary = "Revocar permiso de un rol", description = "Elimina físicamente la asociación de un permiso para el rol indicado. Invalida la caché de permisos de los usuarios afectados.")
  @DeleteMapping("/{permissionId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> revokePermission(
      @Parameter(description = "ID del rol", required = true) @PathVariable UUID roleId,
      @Parameter(description = "ID del permiso a revocar", required = true) @PathVariable UUID permissionId);

}

