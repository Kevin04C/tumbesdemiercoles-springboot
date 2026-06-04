package com.tumbesdemiercoles.api.auth.application.usecase;

import com.tumbesdemiercoles.api.auth.application.dto.AuthTokenReponseDto;
import com.tumbesdemiercoles.api.auth.application.dto.RefreshTokenRequestDto;
import com.tumbesdemiercoles.api.auth.application.dto.UserAuthDto;
import com.tumbesdemiercoles.api.auth.application.ports.In.RefreshTokenUseCase;
import com.tumbesdemiercoles.api.auth.application.ports.out.TokenProviderPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.UserIdentityPort;
import com.tumbesdemiercoles.api.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

  private final TokenProviderPort tokenProvider;
  private final UserIdentityPort userIdentityPort;

  @Override
  public Mono<AuthTokenReponseDto> refreshSession(RefreshTokenRequestDto refreshTokenRequestDto) {
    return tokenProvider.validateAndExtractIdFromRefreshToken(refreshTokenRequestDto.getRefreshToken())
        .switchIfEmpty(Mono.error(() -> new UnauthorizedException("Token inválido o expirado")))

        .flatMap(userId -> userIdentityPort.findById(userId))

        .filter(user -> user.getIsActive())
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