package com.tumbesdemiercoles.api.news.application.usecase;

import com.tumbesdemiercoles.api.news.application.dto.NewsRequestDto;
import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import com.tumbesdemiercoles.api.news.application.ports.in.UpdateNewsUseCase;
import com.tumbesdemiercoles.api.news.application.ports.out.NewsSearchPort;
import com.tumbesdemiercoles.api.news.domain.model.News;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import com.tumbesdemiercoles.api.shared.utils.SlugUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateNewsUseCaseImpl implements UpdateNewsUseCase {

  private final NewsRepository newsRepository;
  private final NewsSearchPort newsSearchPort;

  @Override
  public Mono<NewsResponseDto> execute(UUID id, NewsRequestDto dto) {
    return newsRepository.findById(id)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("News", id)))
        .flatMap(existing -> {
          if (dto.getTitle() == null) {
            return Mono.just(buildUpdated(existing, dto, existing.getSlug()));
          }
          String baseSlug = SlugUtils.toSlug(dto.getTitle());
          return newsRepository.existsBySlugAndIdNot(baseSlug, id)
              .map(exists -> exists ? baseSlug + "-" + randomHex(6) : baseSlug)
              .map(slug -> buildUpdated(existing, dto, slug));
        })
        .flatMap(newsRepository::save)
        .flatMap(saved -> newsSearchPort.updateIndexedNews(saved)
            .onErrorResume(e -> {
              log.error("Failed to update indexed news {}: {}", saved.getId(), e.getMessage());
              return Mono.empty();
            })
            .thenReturn(saved))
        .map(this::toResponse);
  }

  private static String randomHex(int length) {
    return UUID.randomUUID().toString().replace("-", "").substring(0, length);
  }

  private News buildUpdated(News existing, NewsRequestDto dto, String slug) {
    return existing.toBuilder()
        .slug(slug)
        .content(dto.getContent() != null ? dto.getContent() : existing.getContent())
        .isCarousel(dto.getIsCarousel() != null ? dto.getIsCarousel() : existing.getIsCarousel())
        .headline(dto.getHeadline() != null ? dto.getHeadline() : existing.getHeadline())
        .isPremium(dto.getIsPremium() != null ? dto.getIsPremium() : existing.getIsPremium())
        .categoryId(dto.getCategoryId() != null ? dto.getCategoryId() : existing.getCategoryId())
        .title(dto.getTitle() != null ? dto.getTitle() : existing.getTitle())
        .isActive(dto.getIsActive() != null ? dto.getIsActive() : existing.getIsActive())
        .imageUrl(dto.getImageUrl() != null ? dto.getImageUrl() : existing.getImageUrl())
        .isPeruDailyNews(dto.getIsPeruDailyNews() != null ? dto.getIsPeruDailyNews() : existing.getIsPeruDailyNews())
        .isLatestNews(dto.getIsLatestNews() != null ? dto.getIsLatestNews() : existing.getIsLatestNews())
        .build();
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
