package com.tumbesdemiercoles.api.news.application.usecase;

import com.tumbesdemiercoles.api.news.application.ports.in.DeleteNewsUseCase;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteNewsUseCaseImpl implements DeleteNewsUseCase {

  private final NewsRepository newsRepository;

  @Override
  public Mono<Void> execute(UUID id) {
    return newsRepository.existsById(id)
        .filter(exists -> exists)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("News", id)))
        .flatMap(exists -> newsRepository.deleteById(id))
        .then();
  }
}
