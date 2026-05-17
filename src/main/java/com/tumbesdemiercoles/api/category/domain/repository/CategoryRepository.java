package com.tumbesdemiercoles.api.category.domain.repository;

import com.tumbesdemiercoles.api.category.domain.model.Category;
import java.util.UUID;

import com.tumbesdemiercoles.api.category.domain.model.CategoryFilter;
import com.tumbesdemiercoles.api.shared.domain.model.PaginatedResult;
import com.tumbesdemiercoles.api.user.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio de dominio para Category.
 * Define los contratos de operaciones de negocio sin depender de frameworks.
 */
public interface CategoryRepository {

  Mono<Category> save(Category category);

  Mono<Category> findById(UUID id);

  Flux<Category> findAll();

  Mono<Category> deleteById(UUID id);

  Mono<Boolean> existsById(UUID id);
  Mono<PaginatedResult<Category>> findCategories(CategoryFilter filter);
}
