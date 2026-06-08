package com.tumbesdemiercoles.api.columnist.domain.repository;

import com.tumbesdemiercoles.api.columnist.domain.model.Columnist;
import com.tumbesdemiercoles.api.columnist.domain.model.ColumnistFilter;
import com.tumbesdemiercoles.api.shared.domain.model.PaginatedResult;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ColumnistRepository {

  Mono<Columnist> save(Columnist columnist);

  Mono<Columnist> findById(UUID id);

  Flux<Columnist> findAll();

  Mono<Columnist> deleteById(UUID id);

  Mono<Boolean> existsById(UUID id);

  Mono<PaginatedResult<Columnist>> findColumnists(ColumnistFilter filter);

  Mono<List<Columnist>> findLatestColumnists();

  Mono<Columnist> findBySlug(String slug);

  Mono<Boolean> existsBySlug(String slug);

  Mono<Boolean> existsBySlugAndIdNot(String slug, UUID excludeId);
}
