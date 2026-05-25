package com.tumbesdemiercoles.api.news.application.usecase;

import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import com.tumbesdemiercoles.api.news.application.ports.in.GetNewsUseCase;
import com.tumbesdemiercoles.api.news.domain.model.News;
import com.tumbesdemiercoles.api.news.domain.model.NewsFilter;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetNewsUseCaseImpl implements GetNewsUseCase {

  private final NewsRepository newsRepository;

  @Override
  public Mono<NewsResponseDto> getById(UUID id) {
    return newsRepository.findById(id)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("News", id)))
        .map(this::toResponse);
  }

  @Override
  public Mono<PageResponseDto<NewsResponseDto>> findNewsList(NewsFilter filter) {
    return newsRepository.findNewsList(filter)
        .map(paginatedResult -> PageResponseDto.<NewsResponseDto>builder()
            .content(paginatedResult.getContent().stream().map(this::toResponse).toList())
            .page(paginatedResult.getCurrentPage())
            .size(paginatedResult.getPageSize())
            .totalElements(paginatedResult.getTotalElements())
            .totalPages(paginatedResult.getTotalPages())
            .build());
  }

  private NewsResponseDto toResponse(News news) {
    return NewsResponseDto.builder()
        .id(news.getId())
        .content(news.getContent())
        .isCarousel(news.getIsCarousel())
        .headline(news.getHeadline())
        .slug(news.getSlug())
        .isPremium(news.getIsPremium())
        .categoryId(news.getCategoryId())
        .title(news.getTitle())
        .isActive(news.getIsActive())
        .imageUrl(news.getImageUrl())
        .isPeruDailyNews(news.getIsPeruDailyNews())
        .isLatestNews(news.getIsLatestNews())
        .createdAt(news.getCreatedAt())
        .updatedAt(news.getUpdatedAt())
        .build();
  }
}
