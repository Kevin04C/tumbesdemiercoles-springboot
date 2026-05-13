package com.tumbesdemiercoles.api.user.infrastructure.adapter.external;

import com.tumbesdemiercoles.api.auth.application.dto.AuthUserDetailsDto;
import com.tumbesdemiercoles.api.auth.application.dto.AuthUserResponseDto;
import com.tumbesdemiercoles.api.auth.application.ports.out.UserIdentityPort;
import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.ports.in.CreateUserUseCase;
import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserIdentityAdapter implements UserIdentityPort {

  private final CreateUserUseCase createUserUseCase;
  private final UserRepository userRepository;

  @Override
  public Mono<AuthUserResponseDto> createIdentity(String email, String firstName, String lastName, String encondedPassword, String imageUrl) {
    UserRequestDto request = UserRequestDto.builder()
        .email(email)
        .firstName(firstName)
        .lastName(lastName)
        .password(encondedPassword)
        .imageUrl(imageUrl)
        .build();

    return createUserUseCase.execute(request)
        .map(response -> {
          return AuthUserResponseDto.builder()
              .id(response.getId())
              .firstName(response.getFirstName())
              .lastName(response.getLastName())
              .email(response.getEmail())
              .imageUrl(response.getImageUrl())
              .isEmailVerified(response.getIsEmailVerified())
              .isActive(response.getIsActive())
              .build();
        });
  }

  @Override
  public Mono<Boolean> existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public Mono<AuthUserDetailsDto> findByEmailForLogin(String email) {
    return userRepository.findByEmail(email)
        .map(user -> AuthUserDetailsDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .passwordHash(user.getPasswordHash())
            .isActive(user.getIsActive())
            .build());
  }
}
