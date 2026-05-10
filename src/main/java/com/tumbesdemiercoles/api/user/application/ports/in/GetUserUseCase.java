package com.tumbesdemiercoles.api.user.application.ports.in;

import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetUserUseCase {

  public Mono<UserResponseDto> getById(UUID id);

  public Flux<UserResponseDto> getAll();

}
