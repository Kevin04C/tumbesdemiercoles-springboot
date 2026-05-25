package com.tumbesdemiercoles.api.news.domain.repository;

import com.tumbesdemiercoles.api.news.domain.model.News;
import com.tumbesdemiercoles.api.news.domain.model.NewsFilter;
import com.tumbesdemiercoles.api.shared.domain.model.PaginatedResult;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NewsRepository {

  Mono<News> save(News news);

  Mono<News> findById(UUID id);

  Flux<News> findAll();

  Mono<News> deleteById(UUID id);

  Mono<Boolean> existsById(UUID id);

  Mono<PaginatedResult<News>> findNewsList(NewsFilter filter);

  Mono<List<News>> findTopByIsCarousel(int limit);

  Mono<List<News>> findTopByIsPeruDailyNews(int limit);

  Mono<List<News>> findTopByCategoryId(UUID categoryId, int limit);
}
