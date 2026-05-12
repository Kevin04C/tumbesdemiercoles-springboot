package com.tumbesdemiercoles.api.user.application.ports.in;

import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.presentation.dto.response.AuthCreateTokenResponse;
import reactor.core.publisher.Mono;

public interface CreateUserUseCase {

  public Mono<AuthCreateTokenResponse> execute(UserRequestDto dto);

}
