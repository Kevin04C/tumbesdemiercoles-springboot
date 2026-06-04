package com.tumbesdemiercoles.api.news.application.ports.in;

import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import com.tumbesdemiercoles.api.news.domain.model.NewsFilter;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;

import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface GetNewsUseCase {
  Mono<NewsResponseDto> getById(UUID id);
  Mono<NewsResponseDto> getBySlug(String slug);
  Mono<List<NewsResponseDto>> getLatestNews(int limit);
  Mono<PageResponseDto<NewsResponseDto>> findNewsList(NewsFilter filter);
  Mono<PageResponseDto<NewsResponseDto>> getByCategorySlug(String categorySlug, NewsFilter filter);
}
