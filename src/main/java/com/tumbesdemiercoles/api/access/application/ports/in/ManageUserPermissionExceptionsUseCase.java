package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.UpdateUserPermissionExceptionsRequestDto;
import com.tumbesdemiercoles.api.access.application.dto.UserPermissionResponseDto;
import java.util.UUID;
import reactor.core.publisher.Flux;

/**
 * Puerto de entrada para gestionar las excepciones (overrides) de permisos de un usuario.
 */
public interface ManageUserPermissionExceptionsUseCase {

  Flux<UserPermissionResponseDto> execute(UUID userId, UpdateUserPermissionExceptionsRequestDto requestDto);

}
