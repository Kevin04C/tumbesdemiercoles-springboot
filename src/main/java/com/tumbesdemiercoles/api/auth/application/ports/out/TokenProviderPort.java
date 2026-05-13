package com.tumbesdemiercoles.api.auth.application.ports.out;

import com.tumbesdemiercoles.api.auth.application.dto.AuthUserDetailsDto;
import com.tumbesdemiercoles.api.auth.application.dto.AuthUserResponseDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface TokenProviderPort {
  Mono<String> generateToken(AuthUserResponseDto userDto);

  Mono<String> generateToken(AuthUserDetailsDto userDetailsDto);
}
