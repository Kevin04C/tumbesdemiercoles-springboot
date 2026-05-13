package com.tumbesdemiercoles.api.user.infrastructure.adapter.external;

import com.tumbesdemiercoles.api.access.application.ports.out.UserExistencePort;
import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Implementación del puerto de salida UserExistencePort del módulo access.
 * Vive en el módulo user porque tiene acceso al UserRepository.
 */
@Component
@RequiredArgsConstructor
public class UserExistenceAdapter implements UserExistencePort {

  private final UserRepository userRepository;

  @Override
  public Mono<Boolean> existsById(UUID userId) {
    return userRepository.findById(userId)
        .hasElement();
  }

}
