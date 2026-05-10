package com.tumbesdemiercoles.api.user.infrastructure.adapter;

import com.tumbesdemiercoles.api.user.application.ports.out.UserEmailVerificationPort;
import com.tumbesdemiercoles.api.user.infrastructure.repository.UserR2dbcRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserEmailVerificationAdapter implements UserEmailVerificationPort {

  private final UserR2dbcRepository userR2dbcRepository;

  @Override
  public Mono<Void> verifyEmail(UUID userId) {
    return userR2dbcRepository.verifyEmail(userId).then();
  }
}
