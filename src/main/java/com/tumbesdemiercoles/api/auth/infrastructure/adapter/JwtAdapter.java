package com.tumbesdemiercoles.api.auth.infrastructure.adapter;

import com.tumbesdemiercoles.api.auth.application.dto.AuthUserDetailsDto;
import com.tumbesdemiercoles.api.auth.application.dto.AuthUserResponseDto;
import com.tumbesdemiercoles.api.auth.application.ports.out.FetchUserRolesPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.TokenProviderPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.dto.TokenPairDto;
import com.tumbesdemiercoles.api.security.model.UserPrincipal;
import com.tumbesdemiercoles.api.security.utils.JwtUtil;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements TokenProviderPort {

  private final JwtUtil jwtUtil;
  private final FetchUserRolesPort fetchUserRolesPort;

  @Override
  public Mono<String> generateToken(AuthUserResponseDto userDto) {
    return fetchUserRolesPort.getRolesForUser(userDto.getId())
        .map(rolesList -> {
          UserPrincipal principal = UserPrincipal.builder()
              .id(userDto.getId().toString())
              .roles(new HashSet<>(rolesList))
              .build();

          return jwtUtil.generateAccessToken(principal);
        });
  }

  @Override
  public Mono<TokenPairDto> generateToken(AuthUserDetailsDto userDetailsDto) {
    return fetchUserRolesPort.getRolesForUser(userDetailsDto.getId())
        .map(rolesList -> {
          UserPrincipal principal = UserPrincipal.builder()
              .id(userDetailsDto.getId().toString())
              .roles(new HashSet<>(rolesList))
              .build();

          String accessToken = jwtUtil.generateAccessToken(principal);

          String refreshToken = jwtUtil.generateRefreshToken(principal);

          Long expiresIn = jwtUtil.getAccessTokenExpirationInSeconds();

          return TokenPairDto.builder()
              .accessToken(accessToken)
              .refreshToken(refreshToken)
              .expiresIn(expiresIn)
              .roles(new ArrayList<>(rolesList))
              .build();
        });
  }

  @Override
  public Mono<UUID> validateAndExtractIdFromRefreshToken(String refreshToken) {
    return Mono.fromCallable(() -> jwtUtil.isRefreshToken(refreshToken))
        .onErrorResume(e -> Mono.empty())
        .filter(isRefresh -> isRefresh)
        .map(ignored -> jwtUtil.extractUserId(refreshToken))
        .map(UUID::fromString);
  }
}
