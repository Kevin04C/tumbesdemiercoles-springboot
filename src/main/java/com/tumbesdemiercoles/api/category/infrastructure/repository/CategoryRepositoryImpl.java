package com.tumbesdemiercoles.api.category.infrastructure.repository;

import com.tumbesdemiercoles.api.category.domain.model.Category;
import com.tumbesdemiercoles.api.category.domain.model.CategoryFilter;
import com.tumbesdemiercoles.api.category.domain.repository.CategoryRepository;
import com.tumbesdemiercoles.api.category.infrastructure.entity.CategoryEntity;
import com.tumbesdemiercoles.api.category.infrastructure.mapper.CategoryPersistenceMapper;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tumbesdemiercoles.api.shared.domain.model.PaginatedResult;
import com.tumbesdemiercoles.api.shared.infrastructure.database.CriteriaHelper;
import com.tumbesdemiercoles.api.shared.infrastructure.database.R2dbcPaginationHelper;
import com.tumbesdemiercoles.api.shared.infrastructure.mapper.PageableMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

  private final CategoryR2dbcRepository r2dbcRepository;
  private final CategoryPersistenceMapper mapper;
  private final R2dbcPaginationHelper paginationHelper;

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
  public Mono<Category> findBySlug(String slug) {
    return r2dbcRepository.findBySlug(slug)
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

  @Override
  public Mono<Map<String, Category>> findBySlugIn(List<String> slugs) {
    return r2dbcRepository.findBySlugIn(slugs)
        .map(mapper::toDomain)
        .collectMap(Category::getSlug);
  }

  @Override
  public Flux<Category> findAllActive() {
    return r2dbcRepository.findCategoriesActive()
        .map(mapper::toDomain);
  }

  @Override
  public Mono<PaginatedResult<Category>> findCategories(CategoryFilter filter) {
    Criteria criteria = Criteria.empty();

    criteria = CriteriaHelper.addEquals(criteria, "id", filter.getId());
    criteria = CriteriaHelper.addLike(criteria, "description", filter.getDescription());

    Pageable pageable = PageableMapper.toPageable(filter);

    return paginationHelper.getPage(
            criteria,
            pageable,
            CategoryEntity.class,
            mapper::toDomain,
            filter.getPage()
    );
  }

}
