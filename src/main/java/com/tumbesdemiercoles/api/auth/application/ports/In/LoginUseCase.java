package com.tumbesdemiercoles.api.auth.application.ports.In;

import com.tumbesdemiercoles.api.auth.application.dto.AuthTokenReponseDto;
import com.tumbesdemiercoles.api.auth.application.dto.LoginRequestDto;
import reactor.core.publisher.Mono;

public interface LoginUseCase {

  Mono<AuthTokenReponseDto> login(LoginRequestDto loginRequestDto);

}
