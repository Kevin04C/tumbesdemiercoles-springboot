package com.tumbesdemiercoles.api.access.application.ports.in;

import com.tumbesdemiercoles.api.access.application.dto.PermissionResponseDto;
import java.util.UUID;
import reactor.core.publisher.Flux;

/**
 * Puerto de entrada para obtener los permisos efectivos (fusionados) de un usuario.
 */
public interface GetEffectivePermissionsUseCase {

  Flux<PermissionResponseDto> execute(UUID userId);

}
