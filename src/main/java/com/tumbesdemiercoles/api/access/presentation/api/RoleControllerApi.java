package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.RoleCreateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.request.RoleUpdateRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.RoleResponse;
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

@Tag(name = "Role", description = "Endpoints para la gestión de roles de usuario")
@RequestMapping("/api/v1/roles")
public interface RoleControllerApi {

  @Operation(summary = "Obtener todos los roles", description = "Retorna una lista con todos los roles registrados en el sistema.")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<RoleResponse>>> findAllRoles();

  @Operation(summary = "Obtener rol por ID", description = "Busca y retorna la información detallada de un rol específico por su ID.")
  @GetMapping("/{roleId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<RoleResponse>> getRoleById(
      @Parameter(description = "ID único del rol", required = true) @PathVariable UUID roleId);

  @Operation(summary = "Crear un nuevo rol", description = "Registra un nuevo rol en el sistema con un nombre y descripción únicos.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<RoleResponse>> createRole(
      @Valid @RequestBody RoleCreateRequest roleCreateRequest);

  @Operation(summary = "Actualizar un rol existente", description = "Modifica los datos (nombre o descripción) de un rol identificado por su ID.")
  @PatchMapping("/{roleId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<RoleResponse>> updateRole(
      @Parameter(description = "ID del rol a actualizar", required = true) @PathVariable UUID roleId,
      @Valid @RequestBody RoleUpdateRequest roleUpdateRequest);

  @Operation(summary = "Eliminar un rol", description = "Realiza la eliminación lógica o física de un rol por su ID.")
  @DeleteMapping("/{roleId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> deleteRole(
      @Parameter(description = "ID del rol a eliminar", required = true) @PathVariable UUID roleId);

}

