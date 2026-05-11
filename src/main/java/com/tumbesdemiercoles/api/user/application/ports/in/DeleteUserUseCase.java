package com.tumbesdemiercoles.api.user.application.ports.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteUserUseCase {

  public Mono<Void> deleteUser(UUID id);

}
