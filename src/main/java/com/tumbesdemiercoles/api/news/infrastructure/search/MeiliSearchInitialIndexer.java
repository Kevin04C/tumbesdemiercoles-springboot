package com.tumbesdemiercoles.api.news.infrastructure.search;

import com.tumbesdemiercoles.api.news.application.ports.out.NewsSearchPort;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeiliSearchInitialIndexer {

  private final NewsRepository newsRepository;
  private final NewsSearchPort newsSearchPort;

  @Value("${meili.reindex:false}")
  private boolean reindex;

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    if (!reindex) {
      log.info("MeiliSearch reindex disabled, skipping initial indexing");
      return;
    }

    log.info("Starting MeiliSearch initial indexing...");
    newsRepository.findAll()
        .buffer(50)
        .flatMap(batch -> newsSearchPort.indexAll(batch))
        .doOnComplete(() -> log.info("MeiliSearch initial indexing completed"))
        .doOnError(e -> log.error("MeiliSearch initial indexing failed", e))
        .subscribe();
  }
}
