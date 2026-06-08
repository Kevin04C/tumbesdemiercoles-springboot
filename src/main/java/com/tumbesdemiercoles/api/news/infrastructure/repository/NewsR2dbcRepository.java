package com.tumbesdemiercoles.api.news.infrastructure.repository;

import com.tumbesdemiercoles.api.news.application.dto.SitemapGeneralDto;
import com.tumbesdemiercoles.api.news.application.dto.SitemapNewsDto;
import com.tumbesdemiercoles.api.news.infrastructure.entity.NewsEntity;

import java.util.List;
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

  @Query("SELECT COUNT(*) FROM news WHERE slug = :slug")
  Mono<Long> countBySlug(String slug);

  @Query("SELECT COUNT(*) FROM news WHERE slug = :slug AND id != :excludeId")
  Mono<Long> countBySlugAndIdNot(String slug, UUID excludeId);

  @Query("SELECT * FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY category_id ORDER BY created_at DESC) AS rn FROM news WHERE category_id IN (:categoryIds)) sub WHERE rn <= :limit")
  Flux<NewsEntity> findTopByCategoryIds(List<UUID> categoryIds, int limit);

  @Query("SELECT slug, updated_at FROM news WHERE is_active = true ORDER BY created_at DESC")
  Flux<SitemapGeneralDto> findSitemapGeneral();

  @Query("SELECT slug, title, image_url, created_at FROM news WHERE is_active = true AND created_at >= NOW() - INTERVAL '48 hours' ORDER BY created_at DESC")
  Flux<SitemapNewsDto> findSitemapNews();

  @Query("SELECT slug, title, image_url, created_at FROM news WHERE is_active = true ORDER BY created_at DESC LIMIT :limit")
  Flux<SitemapNewsDto> findLatestSitemapNews(int limit);
}
