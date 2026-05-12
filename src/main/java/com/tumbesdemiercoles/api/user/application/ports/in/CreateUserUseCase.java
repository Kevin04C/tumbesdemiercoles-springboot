package com.tumbesdemiercoles.api.user.application.ports.in;

import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import reactor.core.publisher.Mono;

public interface CreateUserUseCase {

  public Mono<UserResponseDto> execute(UserRequestDto dto);

}
