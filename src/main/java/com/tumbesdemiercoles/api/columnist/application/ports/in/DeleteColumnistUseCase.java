package com.tumbesdemiercoles.api.columnist.application.ports.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteColumnistUseCase {
    Mono<Void> execute(UUID id);
}
