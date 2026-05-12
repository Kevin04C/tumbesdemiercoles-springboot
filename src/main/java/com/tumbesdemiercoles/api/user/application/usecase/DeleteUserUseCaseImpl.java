package com.tumbesdemiercoles.api.user.application.usecase;

import com.tumbesdemiercoles.api.user.application.ports.in.DeleteUserUseCase;
import com.tumbesdemiercoles.api.user.domain.exception.UserNotFoundException;
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
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

  private final UserRepository userRepository;

  public Mono<Void> deleteUser(UUID id) {
    return userRepository.findById(id)
        .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
        .flatMap(user -> {
          user.deleteAccount();
          return userRepository.save(user);
        })
        .then();
  }
}