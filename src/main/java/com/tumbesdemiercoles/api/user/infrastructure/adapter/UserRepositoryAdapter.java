package com.tumbesdemiercoles.api.user.infrastructure.adapter;

import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.domain.repository.UserRepository;
import com.tumbesdemiercoles.api.user.infrastructure.mapper.UserPersistenceMapper;
import com.tumbesdemiercoles.api.user.infrastructure.repository.UserR2dbcRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador: implementa la interfaz UserRepository del dominio
 * usando Spring Data R2DBC por debajo.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

  private final UserR2dbcRepository r2dbcRepository;
  private final UserPersistenceMapper mapper;

  @Override
  public Mono<User> save(User user) {
    return r2dbcRepository.save(mapper.toEntity(user))
        .map(mapper::toDomain);
  }

  @Override
  public Mono<User> findById(UUID id) {
    return r2dbcRepository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<User> findByEmail(String email) {
    return r2dbcRepository.findByUserEmail(email)
        .map(mapper::toDomain);
  }

  @Override
  public Flux<User> findAll() {
    return r2dbcRepository.findAll()
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Void> deleteById(UUID id) {
    return r2dbcRepository.deleteById(id);
  }

  @Override
  public Mono<Boolean> existsByEmail(String email) {
    return r2dbcRepository.existsByUserEmail(email);
  }

}
