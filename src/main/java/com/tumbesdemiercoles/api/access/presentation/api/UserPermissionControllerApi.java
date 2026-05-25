package com.tumbesdemiercoles.api.access.presentation.api;

import com.tumbesdemiercoles.api.access.presentation.dto.request.UpdateUserPermissionExceptionsRequest;
import com.tumbesdemiercoles.api.access.presentation.dto.response.PermissionResponse;
import com.tumbesdemiercoles.api.access.presentation.dto.response.UserPermissionResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "User Permission", description = "Endpoints para la gestión de excepciones de permisos y permisos efectivos de usuario")
@RequestMapping("/api/v1/users/{userId}/permissions")
public interface UserPermissionControllerApi {

  @Operation(summary = "Actualizar excepciones de permisos del usuario", description = "Permite habilitar o deshabilitar específicamente permisos para un usuario (Exceptions) sobreescribiendo las reglas generales del rol.")
  @PutMapping("/exceptions")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<UserPermissionResponse>>> updateExceptions(
      @Parameter(description = "ID del usuario", required = true) @PathVariable UUID userId,
      @Valid @RequestBody UpdateUserPermissionExceptionsRequest request);

  @Operation(summary = "Obtener permisos efectivos del usuario", description = "Retorna el conjunto final de permisos activos que posee el usuario (Roles heredados + Excepciones directas).")
  @PreAuthorize("hasAuthority('READ_EFFECTIVE_PERMISSIONS')")
  @GetMapping("/effective")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<PermissionResponse>>> getEffectivePermissions(
      @Parameter(description = "ID del usuario", required = true) @PathVariable UUID userId);

}

