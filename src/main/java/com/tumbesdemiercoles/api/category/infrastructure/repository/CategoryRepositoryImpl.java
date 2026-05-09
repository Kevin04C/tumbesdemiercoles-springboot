package com.tumbesdemiercoles.api.category.infrastructure.repository;

import com.tumbesdemiercoles.api.category.domain.model.Category;
import com.tumbesdemiercoles.api.category.domain.repository.CategoryRepository;
import com.tumbesdemiercoles.api.category.infrastructure.entity.CategoryEntity;
import com.tumbesdemiercoles.api.category.infrastructure.mapper.CategoryPersistenceMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

  private final CategoryR2dbcRepository r2dbcRepository;
  private final CategoryPersistenceMapper mapper;

  @Override
  public Mono<Category> save(Category category) {
    CategoryEntity entity = mapper.toEntity(category);
    return r2dbcRepository.save(entity)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Category> findById(UUID id) {
    return r2dbcRepository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Flux<Category> findAll() {
    return r2dbcRepository.findAll()
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Category> deleteById(UUID id) {
    return r2dbcRepository.findById(id)
        .flatMap(entity -> r2dbcRepository.delete(entity)
            .then(Mono.just(mapper.toDomain(entity))));
  }

  @Override
  public Mono<Boolean> existsById(UUID id) {
    return r2dbcRepository.existsById(id);
  }

}
