package com.tumbesdemiercoles.api.user.application.usecase;

import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Caso de uso: Eliminar un usuario.
 */
@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

  private final UserRepository userRepository;

  public Mono<Void> execute(UUID id) {
    return userRepository.deleteById(id);
  }

}
