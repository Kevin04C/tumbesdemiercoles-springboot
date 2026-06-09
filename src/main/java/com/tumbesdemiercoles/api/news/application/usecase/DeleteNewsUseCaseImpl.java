package com.tumbesdemiercoles.api.news.application.usecase;

import com.tumbesdemiercoles.api.news.application.ports.in.DeleteNewsUseCase;
import com.tumbesdemiercoles.api.news.application.ports.out.NewsSearchPort;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteNewsUseCaseImpl implements DeleteNewsUseCase {

  private final NewsRepository newsRepository;
  private final NewsSearchPort newsSearchPort;

  @Override
  public Mono<Void> execute(UUID id) {
    return newsRepository.existsById(id)
        .filter(exists -> exists)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("News", id)))
        .flatMap(exists -> newsRepository.deleteById(id))
        .flatMap(news -> newsSearchPort.removeIndexedNews(news.getId())
            .onErrorResume(e -> {
              log.error("Failed to remove indexed news {}: {}", news.getId(), e.getMessage());
              return Mono.empty();
            })
            .thenReturn(news))
        .then();
  }
}
