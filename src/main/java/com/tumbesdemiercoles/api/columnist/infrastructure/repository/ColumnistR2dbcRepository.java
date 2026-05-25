package com.tumbesdemiercoles.api.columnist.infrastructure.repository;

import com.tumbesdemiercoles.api.columnist.infrastructure.entity.ColumnistEntity;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ColumnistR2dbcRepository extends ReactiveCrudRepository<ColumnistEntity, UUID> {

  @Query("SELECT * FROM columnist ORDER BY created_at DESC LIMIT 4")
  Flux<ColumnistEntity> findLatestColumnists();
}
