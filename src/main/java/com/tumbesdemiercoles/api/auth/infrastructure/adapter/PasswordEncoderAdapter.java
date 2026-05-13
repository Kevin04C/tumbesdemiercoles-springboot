package com.tumbesdemiercoles.api.auth.infrastructure.adapter;

import com.tumbesdemiercoles.api.auth.application.ports.out.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public Mono<String> encode(String rawPassword) {
    return Mono.fromCallable(() -> passwordEncoder.encode(rawPassword))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<Boolean> matches(String rawPassword, String encodedPassword) {
    return Mono.fromCallable(() -> passwordEncoder.matches(rawPassword, encodedPassword))
        .subscribeOn(Schedulers.boundedElastic());
  }
}
