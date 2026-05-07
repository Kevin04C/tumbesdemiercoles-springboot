package com.tumbesdemiercoles.api.services.definition;

import com.tumbesdemiercoles.api.entities.User;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<User> createUser(User user);
  Mono<User> getUserById(UUID id);
  Mono<User> getUserByCorreo(String correo);
  Flux<User> getAllUsers();

}
