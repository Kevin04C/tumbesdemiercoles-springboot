package com.tumbesdemiercoles.api.auth.application.ports.out;

import com.tumbesdemiercoles.api.auth.application.dto.AuthUserDetailsDto;
import reactor.core.publisher.Mono;

public interface JwtProviderPort {
  Mono<String> generateToken(AuthUserDetailsDto userDetails);
}
