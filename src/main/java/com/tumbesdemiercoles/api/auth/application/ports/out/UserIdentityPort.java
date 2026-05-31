package com.tumbesdemiercoles.api.auth.application.ports.out;

import com.tumbesdemiercoles.api.auth.application.dto.AuthUserDetailsDto;
import com.tumbesdemiercoles.api.auth.application.dto.AuthUserResponseDto;
import reactor.core.publisher.Mono;

public interface UserIdentityPort {

  Mono<AuthUserResponseDto> createIdentity(String email, String firstName, String lastName , String encondedPassword , String imageUrl, String userName);

  Mono<Boolean> existsByEmail(String email);

  Mono<AuthUserDetailsDto> findByEmailForLogin(String email);

}
