package com.tumbesdemiercoles.api.user.application.ports.out;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UserEmailVerificationPort {

  Mono<Void> verifyEmail(UUID userId);

}
