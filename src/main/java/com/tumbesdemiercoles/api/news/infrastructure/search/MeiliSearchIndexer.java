package com.tumbesdemiercoles.api.news.infrastructure.search;

import com.tumbesdemiercoles.api.news.application.ports.out.NewsSearchPort;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeiliSearchIndexer {

  private final NewsRepository newsRepository;
  private final NewsSearchPort newsSearchPort;

  public Mono<Void> reindexAll() {
    log.info("Starting MeiliSearch full reindex...");
    return newsRepository.findAll()
        .buffer(50)
        .flatMap(batch -> newsSearchPort.indexAll(batch))
        .doOnComplete(() -> log.info("MeiliSearch full reindex completed"))
        .doOnError(e -> log.error("MeiliSearch full reindex failed", e))
        .then();
  }
}
