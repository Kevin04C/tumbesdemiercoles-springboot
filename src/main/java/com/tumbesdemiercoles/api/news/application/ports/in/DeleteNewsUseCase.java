package com.tumbesdemiercoles.api.news.application.ports.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteNewsUseCase {
  Mono<Void> execute(UUID id);
}
