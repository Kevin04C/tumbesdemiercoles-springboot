package com.tumbesdemiercoles.api.auth.application.ports.In;

import com.tumbesdemiercoles.api.auth.application.dto.AuthResponseDto;
import com.tumbesdemiercoles.api.auth.application.dto.RegisterRequestDto;
import reactor.core.publisher.Mono;

public interface RegisterUseCase {

  Mono<AuthResponseDto> register(RegisterRequestDto requestDto);

}
