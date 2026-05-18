package com.tumbesdemiercoles.api.access.application.ports.out;

import java.util.UUID;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida para verificar la existencia de un usuario.
 * Será implementado por el módulo user en infrastructure/adapter/external.
 */
public interface UserExistencePort {

  Mono<Boolean> existsById(UUID userId);
}
