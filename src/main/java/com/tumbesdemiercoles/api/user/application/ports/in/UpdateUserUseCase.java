package com.tumbesdemiercoles.api.user.application.ports.in;

import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdateUserUseCase {

   Mono<UserResponseDto> UpdateUser(UUID id, UserRequestDto userRequestDto);
}
