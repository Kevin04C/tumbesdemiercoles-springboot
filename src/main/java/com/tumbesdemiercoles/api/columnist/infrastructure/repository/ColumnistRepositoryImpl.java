package com.tumbesdemiercoles.api.columnist.infrastructure.repository;

import com.tumbesdemiercoles.api.columnist.domain.model.Columnist;
import com.tumbesdemiercoles.api.columnist.domain.model.ColumnistFilter;
import com.tumbesdemiercoles.api.columnist.domain.repository.ColumnistRepository;
import com.tumbesdemiercoles.api.columnist.infrastructure.entity.ColumnistEntity;
import com.tumbesdemiercoles.api.columnist.infrastructure.mapper.ColumnistPersistenceMapper;
import com.tumbesdemiercoles.api.shared.domain.model.PaginatedResult;
import com.tumbesdemiercoles.api.shared.infrastructure.database.CriteriaHelper;
import com.tumbesdemiercoles.api.shared.infrastructure.database.R2dbcPaginationHelper;
import com.tumbesdemiercoles.api.shared.infrastructure.mapper.PageableMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;

@Repository
@RequiredArgsConstructor
public class ColumnistRepositoryImpl implements ColumnistRepository {

  private final ColumnistR2dbcRepository r2dbcRepository;
  private final ColumnistPersistenceMapper mapper;
  private final R2dbcPaginationHelper paginationHelper;

  @Override
  public Mono<Columnist> save(Columnist columnist) {
    ColumnistEntity entity = mapper.toEntity(columnist);
    return r2dbcRepository.save(entity)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Columnist> findById(UUID id) {
    return r2dbcRepository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Flux<Columnist> findAll() {
    return r2dbcRepository.findAll()
        .map(mapper::toDomain);
  }

  @Override
  public Mono<Columnist> deleteById(UUID id) {
    return r2dbcRepository.findById(id)
        .flatMap(entity -> r2dbcRepository.delete(entity)
            .then(Mono.just(mapper.toDomain(entity))));
  }

  @Override
  public Mono<Columnist> findBySlug(String slug) {
    return r2dbcRepository.findBySlug(slug)
        .map(mapper::toDomain)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forSlug("Columnist", slug)));
  }

  @Override
  public Mono<Boolean> existsBySlug(String slug) {
    return r2dbcRepository.countBySlug(slug)
        .map(count -> count > 0);
  }

  @Override
  public Mono<Boolean> existsBySlugAndIdNot(String slug, UUID excludeId) {
    return r2dbcRepository.countBySlugAndIdNot(slug, excludeId)
        .map(count -> count > 0);
  }

  @Override
  public Mono<Boolean> existsById(UUID id) {
    return r2dbcRepository.existsById(id);
  }

  @Override
  public Mono<List<Columnist>> findLatestColumnists() {
    return r2dbcRepository.findLatestColumnists()
        .map(mapper::toDomain)
        .collectList();
  }

  @Override
  public Mono<PaginatedResult<Columnist>> findColumnists(ColumnistFilter filter) {
    Criteria criteria = Criteria.empty();

    criteria = CriteriaHelper.addEquals(criteria, "id", filter.getId());
    criteria = CriteriaHelper.addLike(criteria, "author", filter.getAuthor());
    criteria = CriteriaHelper.addLike(criteria, "title", filter.getTitle());
    criteria = CriteriaHelper.addLike(criteria, "slug", filter.getSlug());

    Pageable pageable = PageableMapper.toPageable(filter);

    return paginationHelper.getPage(
        criteria,
        pageable,
        ColumnistEntity.class,
        mapper::toDomain,
        filter.getPage()
    );
  }
}
