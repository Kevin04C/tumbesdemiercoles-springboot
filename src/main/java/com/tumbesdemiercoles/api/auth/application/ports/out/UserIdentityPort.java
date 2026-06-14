package com.tumbesdemiercoles.api.auth.application.ports.out;

import com.tumbesdemiercoles.api.auth.application.dto.AuthUserDetailsDto;
import com.tumbesdemiercoles.api.auth.application.dto.AuthUserResponseDto;
import com.tumbesdemiercoles.api.auth.application.ports.out.dto.UserIdentityDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UserIdentityPort {

  Mono<AuthUserResponseDto> createIdentity(UserIdentityDto userIdentityDto);

  Mono<Boolean> existsByEmail(String email);

  Mono<AuthUserDetailsDto> findByEmailForLogin(String email);

  Mono<AuthUserDetailsDto> findById(UUID id);

}
