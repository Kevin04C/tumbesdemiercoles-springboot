package com.tumbesdemiercoles.api.repository;

import com.tumbesdemiercoles.api.entities.User;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

  Mono<User> findByUserEmail(String userEmail);

}
