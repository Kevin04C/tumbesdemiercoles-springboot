package com.tumbesdemiercoles.api.digitalweekly.application.ports.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteDigitalWeeklyUseCase {
  Mono<Void> execute(UUID id);
}
