package com.tumbesdemiercoles.api.services.implementation;

import com.tumbesdemiercoles.api.entities.User;
import com.tumbesdemiercoles.api.repository.UserRepository;
import com.tumbesdemiercoles.api.services.definition.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {

  private final UserRepository userRepository;

  @Override
  public Flux<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public Mono<User> getUserById(UUID id) {
    return userRepository.findById(id);
  }

  @Override
  public Mono<User> getUserByCorreo(String correo) {
    return userRepository.findByUserEmail(correo);
  }

  @Override
  public Mono<User> createUser(User user) {
    return null;
  }

}
