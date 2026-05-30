package com.tumbesdemiercoles.api.category.infrastructure.repository;

import com.tumbesdemiercoles.api.category.infrastructure.entity.CategoryEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio de Spring Data R2DBC para la entidad CategoryEntity.
 * Solo existe en la capa de infraestructura.
 */
public interface CategoryR2dbcRepository extends ReactiveCrudRepository<CategoryEntity, UUID> {

  Mono<CategoryEntity> findBySlug(String slug);

  @Query("SELECT * FROM category WHERE slug IN (:slugs)")
  Flux<CategoryEntity> findBySlugIn(List<String> slugs);

  @Query("SELECT * FROM category WHERE is_active = true")
  Flux<CategoryEntity> findCategoriesActive();
}
