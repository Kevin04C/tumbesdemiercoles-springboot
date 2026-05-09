package com.tumbesdemiercoles.api.user.domain.repository;

import com.tumbesdemiercoles.api.user.domain.model.User;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de persistencia del dominio.
 * Define QUÉ operaciones necesita el caso de uso.
 * La implementación concreta vive en infraestructura (Adapter).
 */
public interface UserRepository {

  Mono<User> save(User user);

  Mono<User> findById(UUID id);

  Mono<User> findByEmail(String email);

  Flux<User> findAll();

  Mono<Void> deleteById(UUID id);

}
