package com.tumbesdemiercoles.api.auth.application.usecase;

import com.tumbesdemiercoles.api.auth.application.dto.AuthTokenReponseDto;
import com.tumbesdemiercoles.api.auth.application.dto.AuthUserDetailsDto;
import com.tumbesdemiercoles.api.auth.application.dto.LoginRequestDto;
import com.tumbesdemiercoles.api.auth.application.dto.UserAuthDto;
import com.tumbesdemiercoles.api.auth.application.ports.In.LoginUseCase;
import com.tumbesdemiercoles.api.auth.application.ports.out.JwtProviderPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.PasswordEncoderPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.TokenProviderPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.UserIdentityPort;
import com.tumbesdemiercoles.api.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {

  private final UserIdentityPort userIdentityPort;
  private final PasswordEncoderPort passwordEncoder;
  private final TokenProviderPort tokenProvider;

  @Override
  public Mono<AuthTokenReponseDto> login(LoginRequestDto loginRequestDto) {
    return userIdentityPort.findByEmailForLogin(loginRequestDto.getEmail())
        .switchIfEmpty(Mono.error(() -> new UnauthorizedException("Credenciales inválidas")))
        .filterWhen(userDetails -> passwordEncoder.matches(loginRequestDto.getPassword(), userDetails.getPasswordHash()))
        .switchIfEmpty(Mono.error(() -> new UnauthorizedException("Credenciales inválidas")))
        .filter(AuthUserDetailsDto::getIsActive)
        .switchIfEmpty(Mono.error(() -> new UnauthorizedException("El usuario se encuentra inactivo")))
        .flatMap(userDetails -> tokenProvider.generateToken(userDetails)
            .map(tokenPair -> AuthTokenReponseDto.builder()
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .expiresIn(tokenPair.getExpiresIn())
                .tokenType("Bearer")
                .user(UserAuthDto.builder()
                    .id(userDetails.getId())
                    .email(userDetails.getEmail())
                    .userName(userDetails.getUserName())
                    .roles(tokenPair.getRoles())
                    .build()
                )
                .build()
            )
        );
  }
}
