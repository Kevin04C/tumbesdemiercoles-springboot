package com.tumbesdemiercoles.api.user.application.ports.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface VerifyEmailUseCase {

  Mono<Void> execute(UUID userId);

}
