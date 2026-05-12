package com.tumbesdemiercoles.api.user.application.usecase;

import com.tumbesdemiercoles.api.user.application.ports.in.VerifyEmailUseCase;
import com.tumbesdemiercoles.api.user.application.ports.out.UserEmailVerificationPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VerifyEmailUseCaseImpl implements VerifyEmailUseCase {

  private final UserEmailVerificationPort userEmailVerificationPort;

  @Override
  public Mono<Void> execute(UUID userId) {
    return userEmailVerificationPort.verifyEmail(userId);
  }
}
