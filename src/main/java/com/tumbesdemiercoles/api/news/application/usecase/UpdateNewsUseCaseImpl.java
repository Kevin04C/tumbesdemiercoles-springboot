package com.tumbesdemiercoles.api.news.application.usecase;

import com.tumbesdemiercoles.api.news.application.dto.NewsRequestDto;
import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import com.tumbesdemiercoles.api.news.application.ports.in.UpdateNewsUseCase;
import com.tumbesdemiercoles.api.news.domain.model.News;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateNewsUseCaseImpl implements UpdateNewsUseCase {

  private final NewsRepository newsRepository;

  @Override
  public Mono<NewsResponseDto> execute(UUID id, NewsRequestDto dto) {
    return newsRepository.findById(id)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("News", id)))
        .map(existing -> existing.toBuilder()
            .content(dto.getContent() != null ? dto.getContent() : existing.getContent())
            .isCarousel(dto.getIsCarousel() != null ? dto.getIsCarousel() : existing.getIsCarousel())
            .headline(dto.getHeadline() != null ? dto.getHeadline() : existing.getHeadline())
            .slug(dto.getSlug() != null ? dto.getSlug() : existing.getSlug())
            .isPremium(dto.getIsPremium() != null ? dto.getIsPremium() : existing.getIsPremium())
            .categoryId(dto.getCategoryId() != null ? dto.getCategoryId() : existing.getCategoryId())
            .title(dto.getTitle() != null ? dto.getTitle() : existing.getTitle())
            .isActive(dto.getIsActive() != null ? dto.getIsActive() : existing.getIsActive())
            .imageUrl(dto.getImageUrl() != null ? dto.getImageUrl() : existing.getImageUrl())
            .isPeruDailyNews(dto.getIsPeruDailyNews() != null ? dto.getIsPeruDailyNews() : existing.getIsPeruDailyNews())
            .isLatestNews(dto.getIsLatestNews() != null ? dto.getIsLatestNews() : existing.getIsLatestNews())
            .build())
        .flatMap(newsRepository::save)
        .map(this::toResponse);
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
