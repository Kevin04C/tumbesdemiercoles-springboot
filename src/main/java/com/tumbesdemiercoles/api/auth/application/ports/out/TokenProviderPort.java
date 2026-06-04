package com.tumbesdemiercoles.api.auth.application.ports.out;

import com.tumbesdemiercoles.api.auth.application.dto.AuthUserDetailsDto;
import com.tumbesdemiercoles.api.auth.application.dto.AuthUserResponseDto;
import com.tumbesdemiercoles.api.auth.application.ports.out.dto.TokenPairDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface TokenProviderPort {
  Mono<String> generateToken(AuthUserResponseDto userDto);

  Mono<TokenPairDto> generateToken(AuthUserDetailsDto userDetailsDto);

  Mono<UUID> validateAndExtractIdFromRefreshToken(String refreshToken);
}
