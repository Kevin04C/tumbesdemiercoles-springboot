package com.tumbesdemiercoles.api.user.presentation.api;

import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserFilterRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserUpdateRequest;
import com.tumbesdemiercoles.api.user.presentation.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@Tag(name = "User", description = "Endpoints para la gestión y consulta de usuarios del sistema")
@RequestMapping("/api/v1/users")
public interface UserControllerApi {

  @Operation(summary = "Listar usuarios con filtros y paginación", description = "Retorna una lista paginada de usuarios basada en criterios de filtro (búsqueda por nombre, correo, etc.).")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<PageResponseDto<UserResponse>>> findUsers(
      @Valid UserFilterRequest userFilterRequest);

  @Operation(summary = "Obtener usuario por ID", description = "Busca y retorna el detalle de un usuario determinado mediante su identificador único.")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<UserResponse>> getUserById(
      @Parameter(description = "ID del usuario", required = true) @PathVariable UUID id);

  @Operation(summary = "Actualizar información del usuario", description = "Permite modificar los datos básicos de perfil de un usuario existente.")
  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<UserResponse>> updateUser(
      @Parameter(description = "ID del usuario", required = true) @PathVariable UUID id,
      @Valid @RequestBody UserUpdateRequest userUpdateRequest);

  @Operation(summary = "Eliminar usuario", description = "Realiza la inactivación lógica o eliminación de un usuario por su ID.")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> deleteUser(
      @Parameter(description = "ID del usuario a eliminar", required = true) @PathVariable UUID id);
}

