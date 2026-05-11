package com.tumbesdemiercoles.api.user.application.ports.in;

import com.tumbesdemiercoles.api.shared.dto.PageResponseDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.presentation.dto.request.UserFilterRequest;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetUserUseCase {

  public Mono<UserResponseDto> getById(UUID id);

  Mono<PageResponseDto<User>> findUsers(UserFilterRequest filter);

}
