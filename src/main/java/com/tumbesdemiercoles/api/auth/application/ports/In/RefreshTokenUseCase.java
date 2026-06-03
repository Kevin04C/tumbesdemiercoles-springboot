package com.tumbesdemiercoles.api.auth.application.ports.In;

import com.tumbesdemiercoles.api.auth.application.dto.AuthTokenReponseDto;
import com.tumbesdemiercoles.api.auth.application.dto.RefreshTokenRequestDto;
import reactor.core.publisher.Mono;

public interface RefreshTokenUseCase {
  Mono<AuthTokenReponseDto> refreshSession(RefreshTokenRequestDto refreshTokenRequestDto);

}
