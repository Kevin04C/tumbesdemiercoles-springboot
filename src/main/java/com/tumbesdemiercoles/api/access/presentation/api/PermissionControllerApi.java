package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.PermissionCreateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.request.PermissionUpdateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.PermissionResponse;
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

@Tag(name = "Permission", description = "Endpoints para la gestión de permisos individuales en el sistema")
@RequestMapping("/api/v1/permissions")
public interface PermissionControllerApi {

  @Operation(summary = "Listar todos los permisos", description = "Retorna el catálogo completo de permisos registrados en la plataforma.")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<PermissionResponse>>> findAllPermissions();

  @Operation(summary = "Obtener permiso por ID", description = "Recupera la información de un permiso específico mediante su identificador único.")
  @GetMapping("/{permissionId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<PermissionResponse>> getPermissionById(
      @Parameter(description = "ID del permiso", required = true) @PathVariable UUID permissionId);

  @Operation(summary = "Registrar nuevo permiso", description = "Crea un nuevo permiso con nombre y descripción técnicos para futuras asignaciones a roles.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<PermissionResponse>> createPermission(
      @Valid @RequestBody PermissionCreateRequest permissionCreateRequest);

  @Operation(summary = "Actualizar permiso", description = "Modifica la información básica o técnica de un permiso existente.")
  @PatchMapping("/{permissionId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<PermissionResponse>> updatePermission(
      @Parameter(description = "ID del permiso a actualizar", required = true) @PathVariable UUID permissionId,
      @Valid @RequestBody PermissionUpdateRequest permissionUpdateRequest);

  @Operation(summary = "Eliminar permiso", description = "Remueve un permiso del catálogo general por su ID.")
  @DeleteMapping("/{permissionId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> deletePermission(
      @Parameter(description = "ID del permiso a eliminar", required = true) @PathVariable UUID permissionId);

}

