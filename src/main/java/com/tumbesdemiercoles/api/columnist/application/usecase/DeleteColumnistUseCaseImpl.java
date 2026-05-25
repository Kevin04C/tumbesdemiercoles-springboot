package com.tumbesdemiercoles.api.columnist.application.usecase;

import com.tumbesdemiercoles.api.columnist.application.ports.in.DeleteColumnistUseCase;
import com.tumbesdemiercoles.api.columnist.domain.repository.ColumnistRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteColumnistUseCaseImpl implements DeleteColumnistUseCase {

  private final ColumnistRepository columnistRepository;

  public Mono<Void> execute(UUID id) {
    return columnistRepository.existsById(id)
        .filter(exists -> exists)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("Columnist", id)))
        .flatMap(exists -> columnistRepository.deleteById(id))
        .then();
  }
}
