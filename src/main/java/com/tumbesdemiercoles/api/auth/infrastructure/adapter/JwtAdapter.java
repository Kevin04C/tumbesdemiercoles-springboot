package com.tumbesdemiercoles.api.auth.infrastructure.adapter;

import com.tumbesdemiercoles.api.auth.application.dto.AuthUserDetailsDto;
import com.tumbesdemiercoles.api.auth.application.dto.AuthUserResponseDto;
import com.tumbesdemiercoles.api.auth.application.ports.out.TokenProviderPort;
import com.tumbesdemiercoles.api.security.model.UserPrincipal;
import com.tumbesdemiercoles.api.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements TokenProviderPort {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<String> generateToken(AuthUserResponseDto userDto) {
        return Mono.fromCallable(() -> {

            UserPrincipal principal = UserPrincipal.builder()
                    .id(userDto.getId().toString())
                    .build();

            return jwtUtil.generateToken(principal);
        });
    }

  @Override
  public Mono<String> generateToken(AuthUserDetailsDto userDetailsDto) {
    return Mono.fromCallable(() -> {
      UserPrincipal principal = UserPrincipal.builder()
          .id(userDetailsDto.getId().toString())
          .build();
      return jwtUtil.generateToken(principal);
    });
  }
}
