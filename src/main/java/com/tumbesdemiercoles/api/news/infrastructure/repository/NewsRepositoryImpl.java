package com.tumbesdemiercoles.api.news.infrastructure.repository;

import com.tumbesdemiercoles.api.news.domain.model.News;
import com.tumbesdemiercoles.api.news.domain.model.NewsFilter;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import com.tumbesdemiercoles.api.news.infrastructure.entity.NewsEntity;
import com.tumbesdemiercoles.api.news.infrastructure.mapper.NewsPersistenceMapper;
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

@Repository
@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepository {

  private final NewsR2dbcRepository r2dbcRepository;
  private final NewsPersistenceMapper mapper;
  private final R2dbcPaginationHelper paginationHelper;

  @Override
  public Mono<News> save(News news) {
    NewsEntity entity = mapper.toEntity(news);
    return r2dbcRepository.save(entity)
        .map(mapper::toDomain);
  }

  @Override
  public Mono<News> findById(UUID id) {
    return r2dbcRepository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public Flux<News> findAll() {
    return r2dbcRepository.findAll()
        .map(mapper::toDomain);
  }

  @Override
  public Mono<News> deleteById(UUID id) {
    return r2dbcRepository.findById(id)
        .flatMap(entity -> r2dbcRepository.delete(entity)
            .then(Mono.just(mapper.toDomain(entity))));
  }

  @Override
  public Mono<Boolean> existsById(UUID id) {
    return r2dbcRepository.existsById(id);
  }

  @Override
  public Mono<PaginatedResult<News>> findNewsList(NewsFilter filter) {
    Criteria criteria = Criteria.empty();

    criteria = CriteriaHelper.addEquals(criteria, "id", filter.getId());
    criteria = CriteriaHelper.addLike(criteria, "slug", filter.getSlug());
    criteria = CriteriaHelper.addLike(criteria, "title", filter.getTitle());
    criteria = CriteriaHelper.addEquals(criteria, "is_active", filter.getIsActive());
    criteria = CriteriaHelper.addEquals(criteria, "category_id", filter.getCategoryId());
    criteria = CriteriaHelper.addEquals(criteria, "is_premium", filter.getIsPremium());
    criteria = CriteriaHelper.addEquals(criteria, "is_carousel", filter.getIsCarousel());
    criteria = CriteriaHelper.addEquals(criteria, "is_peru_daily_news", filter.getIsPeruDailyNews());
    criteria = CriteriaHelper.addEquals(criteria, "is_latest_news", filter.getIsLatestNews());

    Pageable pageable = PageableMapper.toPageable(filter);

    return paginationHelper.getPage(
        criteria,
        pageable,
        NewsEntity.class,
        mapper::toDomain,
        filter.getPage()
    );
  }

  @Override
  public Mono<List<News>> findTopByIsCarousel(int limit) {
    return r2dbcRepository.findTopByIsCarousel(limit)
        .map(mapper::toDomain)
        .collectList();
  }

  @Override
  public Mono<List<News>> findTopByIsPeruDailyNews(int limit) {
    return r2dbcRepository.findTopByIsPeruDailyNews(limit)
        .map(mapper::toDomain)
        .collectList();
  }

  @Override
  public Mono<List<News>> findTopByCategoryId(UUID categoryId, int limit) {
    return r2dbcRepository.findTopByCategoryId(categoryId, limit)
        .map(mapper::toDomain)
        .collectList();
  }
}
