package com.tumbesdemiercoles.api.user.application.usecase;

import com.tumbesdemiercoles.api.shared.exception.ConflictException;
import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.application.ports.in.CreateUserUseCase;
import com.tumbesdemiercoles.api.user.domain.event.UserRegisteredEvent;
import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Caso de uso: Crear un nuevo usuario.
 */
@Service
@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

  private final UserRepository userRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public Mono<UserResponseDto> execute(UserRequestDto dto) {

    return userRepository.existsByEmail(dto.getEmail())
        .filter(exists -> !exists)
        .switchIfEmpty(Mono.error(() -> ConflictException.forDuplicate("User", "email", dto.getEmail())))
        .flatMap(isEmailAvailable -> {
          User user = User.createNewUser(
              dto.getFirstName(),
              dto.getLastName(),
              dto.getEmail(),
              dto.getPassword(),
              dto.getImageUrl()
          );
          System.out.println("1. DOMINIO ANTES DE BD - isEmailVerified: " + user.getIsEmailVerified());
          return userRepository.save(user).doOnNext(savedUser -> {

            // TRAMPA 2: Verificamos qué nos devuelve el R2DBC (Base de datos)
            System.out.println("2. DESPUÉS DE BD - isEmailVerified: " + savedUser.getIsEmailVerified());
          });
        })
        .doOnSuccess(user -> {
          eventPublisher.publishEvent(new UserRegisteredEvent(user));
        })
        .map(this::toResponse);
  }

  private UserResponseDto toResponse(User user) {
    return UserResponseDto.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .imageUrl(user.getImageUrl())
        .isEmailVerified(user.getIsEmailVerified())
        .isActive(user.getIsActive())
        .build();
  }
}