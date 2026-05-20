package com.tumbesdemiercoles.api.digitalweekly.application.usecase;

import com.tumbesdemiercoles.api.digitalweekly.application.ports.in.DeleteDigitalWeeklyUseCase;
import com.tumbesdemiercoles.api.digitalweekly.domain.repository.DigitalWeeklyRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteDigitalWeeklyUseCaseImpl implements DeleteDigitalWeeklyUseCase {

  private final DigitalWeeklyRepository digitalWeeklyRepository;

  @Override
  public Mono<Void> execute(UUID id) {
    return digitalWeeklyRepository.existsById(id)
        .filter(exists -> exists)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("DigitalWeekly", id)))
        .flatMap(exists -> digitalWeeklyRepository.deleteById(id))
        .then();
  }
}
