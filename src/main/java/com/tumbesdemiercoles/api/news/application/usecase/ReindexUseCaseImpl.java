package com.tumbesdemiercoles.api.news.application.usecase;

import com.tumbesdemiercoles.api.news.application.ports.in.ReindexUseCase;
import com.tumbesdemiercoles.api.news.infrastructure.search.MeiliSearchIndexer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReindexUseCaseImpl implements ReindexUseCase {

  private final MeiliSearchIndexer meiliSearchIndexer;

  @Override
  public Mono<Void> execute() {
    return meiliSearchIndexer.reindexAll();
  }
}
