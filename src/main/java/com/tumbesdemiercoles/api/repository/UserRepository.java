package com.tumbesdemiercoles.api.repository;

import com.tumbesdemiercoles.api.entities.User;
import java.util.UUID;

import com.tumbesdemiercoles.api.entities.dtos.UserAuthorityDto;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

  Mono<User> findByUserEmail(String userEmail);

  Flux<UserAuthorityDto> findAuthoritiesByUserId(UUID userId);

}
