package com.tumbesdemiercoles.api.digitalweekly.domain.repository;

import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeekly;
import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeeklyFilter;
import com.tumbesdemiercoles.api.shared.domain.model.PaginatedResult;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio de dominio para DigitalWeekly.
 * Define los contratos de operaciones de negocio sin depender de frameworks.
 */
public interface DigitalWeeklyRepository {

  Mono<DigitalWeekly> save(DigitalWeekly digitalWeekly);

  Mono<DigitalWeekly> findById(UUID id);

  Flux<DigitalWeekly> findAll();

  Mono<DigitalWeekly> deleteById(UUID id);

  Mono<Boolean> existsById(UUID id);

  Mono<PaginatedResult<DigitalWeekly>> findDigitalWeeklies(DigitalWeeklyFilter filter);
}
