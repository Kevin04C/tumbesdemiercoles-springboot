package com.tumbesdemiercoles.api.news.infrastructure.repository;

import com.tumbesdemiercoles.api.news.infrastructure.entity.NewsEntity;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NewsR2dbcRepository extends ReactiveCrudRepository<NewsEntity, UUID> {

  @Query("SELECT * FROM news WHERE is_carousel = true ORDER BY created_at DESC LIMIT :limit")
  Flux<NewsEntity> findTopByIsCarousel(int limit);

  @Query("SELECT * FROM news WHERE is_peru_daily_news = true ORDER BY created_at DESC LIMIT :limit")
  Flux<NewsEntity> findTopByIsPeruDailyNews(int limit);

  @Query("SELECT * FROM news WHERE category_id = :categoryId ORDER BY created_at DESC LIMIT :limit")
  Flux<NewsEntity> findTopByCategoryId(UUID categoryId, int limit);

  @Query("SELECT * FROM news WHERE is_latest_news = true ORDER BY created_at DESC LIMIT :limit")
  Flux<NewsEntity> findTopByIsLatestNews(int limit);

  @Query("SELECT * FROM news WHERE status_registry = 'ACTIVE' AND id != :excludeId AND category_id = :categoryId ORDER BY created_at DESC LIMIT :limit")
  Flux<NewsEntity> findRelatedByCategory(UUID excludeId, UUID categoryId, int limit);

  @Query("SELECT * FROM news WHERE status_registry = 'ACTIVE' AND id != :excludeId AND (:categoryId IS NULL OR category_id != :categoryId) ORDER BY created_at DESC LIMIT :limit")
  Flux<NewsEntity> findRecentExcludingCategory(UUID excludeId, UUID categoryId, int limit);

  @Query("SELECT * FROM news WHERE slug = :slug")
  Mono<NewsEntity> findBySlug(String slug);
}
