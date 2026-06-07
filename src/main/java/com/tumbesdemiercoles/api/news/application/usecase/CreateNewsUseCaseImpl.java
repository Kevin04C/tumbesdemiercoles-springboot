package com.tumbesdemiercoles.api.news.application.usecase;

import com.tumbesdemiercoles.api.news.application.dto.NewsRequestDto;
import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import com.tumbesdemiercoles.api.news.application.ports.in.CreateNewsUseCase;
import com.tumbesdemiercoles.api.news.domain.model.News;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import com.tumbesdemiercoles.api.shared.utils.SlugUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateNewsUseCaseImpl implements CreateNewsUseCase {

  private final NewsRepository newsRepository;

  @Override
  public Mono<NewsResponseDto> execute(NewsRequestDto dto) {
    String baseSlug = SlugUtils.toSlug(dto.getTitle());
    return newsRepository.existsBySlug(baseSlug)
        .map(exists -> exists ? baseSlug + "-" + randomHex(6) : baseSlug)
        .flatMap(slug -> {
          News news = News.builder()
              .slug(slug)
              .content(dto.getContent())
              .isCarousel(dto.getIsCarousel())
              .headline(dto.getHeadline())
              .isPremium(dto.getIsPremium() != null ? dto.getIsPremium() : false)
              .categoryId(dto.getCategoryId())
              .title(dto.getTitle())
              .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
              .imageUrl(dto.getImageUrl())
              .isPeruDailyNews(dto.getIsPeruDailyNews() != null ? dto.getIsPeruDailyNews() : false)
              .isLatestNews(dto.getIsLatestNews() != null ? dto.getIsLatestNews() : false)
              .build();
          return newsRepository.save(news);
        })
        .map(this::toResponse);
  }

  private static String randomHex(int length) {
    return UUID.randomUUID().toString().replace("-", "").substring(0, length);
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
