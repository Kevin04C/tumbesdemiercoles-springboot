package com.tumbesdemiercoles.api.news.application.ports.in;

import reactor.core.publisher.Mono;

public interface ReindexUseCase {

  Mono<Void> execute();
}
