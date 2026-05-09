package com.tumbesdemiercoles.api.category.application.usecase;

import com.tumbesdemiercoles.api.category.domain.repository.CategoryRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

  private final CategoryRepository categoryRepository;

  public Mono<Void> execute(UUID id) {
    return categoryRepository.existsById(id)
        .filter(exists -> exists)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("Category", id)))
        .flatMap(exists -> categoryRepository.deleteById(id))
        .then();
  }

}
