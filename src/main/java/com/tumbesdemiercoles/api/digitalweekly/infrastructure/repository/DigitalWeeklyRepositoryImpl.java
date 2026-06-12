package com.tumbesdemiercoles.api.digitalweekly.infrastructure.repository;

import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeekly;
import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeeklyFilter;
import com.tumbesdemiercoles.api.digitalweekly.domain.repository.DigitalWeeklyRepository;
import com.tumbesdemiercoles.api.digitalweekly.infrastructure.entity.DigitalWeeklyEntity;
import com.tumbesdemiercoles.api.digitalweekly.infrastructure.mapper.DigitalWeeklyPersistenceMapper;
import com.tumbesdemiercoles.api.shared.domain.model.PaginatedResult;
import com.tumbesdemiercoles.api.shared.infrastructure.database.CriteriaHelper;
import com.tumbesdemiercoles.api.shared.infrastructure.database.R2dbcPaginationHelper;
import com.tumbesdemiercoles.api.shared.infrastructure.mapper.PageableMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class DigitalWeeklyRepositoryImpl implements DigitalWeeklyRepository {

  private final DigitalWeeklyR2dbcRepository r2dbcRepository;
  private final DigitalWeeklyPersistenceMapper mapper;
  private final R2dbcPaginationHelper paginationHelper;

  @Override
  public Mono<DigitalWeekly> save(DigitalWeekly digitalWeekly) {
    DigitalWeeklyEntity entity = mapper.toEntity(digitalWeekly);
    return r2dbcRepository.save(entity)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<DigitalWeekly> findById(UUID id) {
    return r2dbcRepository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Flux<DigitalWeekly> findAll() {
    return r2dbcRepository.findAll()
        .map(mapper::toDomain);
  }

  @Override
  public Mono<DigitalWeekly> deleteById(UUID id) {
    return r2dbcRepository.findById(id)
        .flatMap(entity -> r2dbcRepository.delete(entity)
            .then(Mono.just(mapper.toDomain(entity))));
  }

  @Override
  public Mono<Boolean> existsById(UUID id) {
    return r2dbcRepository.existsById(id);
  }

  @Override
  public Mono<DigitalWeekly> findLatest() {
    return r2dbcRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
        .map(mapper::toDomain);
  }

  @Override
  public Mono<PaginatedResult<DigitalWeekly>> findDigitalWeeklies(DigitalWeeklyFilter filter) {
    Criteria criteria = Criteria.empty();

    criteria = CriteriaHelper.addEquals(criteria, "id", filter.getId());
    criteria = CriteriaHelper.addLike(criteria, "descripcion", filter.getDescripcion());
    criteria = CriteriaHelper.addEquals(criteria, "is_active", filter.getIsActive());
    criteria = CriteriaHelper.addEquals(criteria, "is_premium", filter.getIsPremium());

    Pageable pageable = PageableMapper.toPageable(filter);

    return paginationHelper.getPage(
        criteria,
        pageable,
        DigitalWeeklyEntity.class,
        mapper::toDomain,
        filter.getPage()
    );
  }
}
