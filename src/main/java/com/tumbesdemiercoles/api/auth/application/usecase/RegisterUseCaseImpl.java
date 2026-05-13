package com.tumbesdemiercoles.api.auth.application.usecase;

import com.tumbesdemiercoles.api.auth.application.dto.AuthResponseDto;
import com.tumbesdemiercoles.api.auth.application.dto.RegisterRequestDto;
import com.tumbesdemiercoles.api.auth.application.ports.In.RegisterUseCase;
import com.tumbesdemiercoles.api.auth.application.ports.out.TokenProviderPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.UserIdentityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegisterUseCaseImpl implements RegisterUseCase {

  private final UserIdentityPort userIdentityPort;
  private final PasswordEncoder passwordEncoder;
  private final TokenProviderPort tokenProviderPort;

  @Override
  public Mono<AuthResponseDto> register(RegisterRequestDto registerRequestDto) {
      String encodedPassword = passwordEncoder.encode(registerRequestDto.getPassword());

      return userIdentityPort.createIdentity(
          registerRequestDto.getEmail(),
          registerRequestDto.getFirstName(),
          registerRequestDto.getLastName(),
          encodedPassword,
          registerRequestDto.getImageUrl()
      ).flatMap(userDto -> {

        return tokenProviderPort.generateToken(userDto)
            .map(token -> AuthResponseDto.builder()
                .token(token)
                .user(userDto)
                .build());
      });
  }
}
