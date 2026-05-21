package com.tumbesdemiercoles.api.common.application.usecase;

import com.tumbesdemiercoles.api.category.domain.repository.CategoryRepository;
import com.tumbesdemiercoles.api.columnist.application.dto.ColumnistResponseDto;
import com.tumbesdemiercoles.api.columnist.domain.model.Columnist;
import com.tumbesdemiercoles.api.columnist.domain.repository.ColumnistRepository;
import com.tumbesdemiercoles.api.common.application.dto.CategoryFeedItemDto;
import com.tumbesdemiercoles.api.common.application.dto.FeedResponseDto;
import com.tumbesdemiercoles.api.digitalweekly.application.dto.DigitalWeeklyResponseDto;
import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeekly;
import com.tumbesdemiercoles.api.digitalweekly.domain.repository.DigitalWeeklyRepository;
import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import com.tumbesdemiercoles.api.news.domain.model.News;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import com.tumbesdemiercoles.api.shared.constants.categories.CategoriesSlugConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetFeedUseCase {

  private final NewsRepository newsRepository;
  private final CategoryRepository categoryRepository;
  private final ColumnistRepository columnistRepository;
  private final DigitalWeeklyRepository digitalWeeklyRepository;

  private static final List<String> BY_CATEGORY_SLUGS = List.of(
      CategoriesSlugConst.POLITICA,
      CategoriesSlugConst.POLICIALES,
      CategoriesSlugConst.PERU,
      CategoriesSlugConst.DEPORTIVAS,
      CategoriesSlugConst.FRONTERA,
      CategoriesSlugConst.TUMBES,
      CategoriesSlugConst.CONTRALMIRANTE_VILLAR,
      CategoriesSlugConst.ZORRITOS
  );

  public Mono<FeedResponseDto> execute() {
    Mono<List<NewsResponseDto>> carousel = newsRepository.findTopByIsCarousel(10)
        .map(list -> list.stream().map(this::toDto).toList());

    Mono<List<NewsResponseDto>> peruDaily = newsRepository.findTopByIsPeruDailyNews(3)
        .map(list -> list.stream().map(this::toDto).toList());

    Mono<List<CategoryFeedItemDto>> byCategory = categoryRepository.findBySlugIn(BY_CATEGORY_SLUGS)
        .flatMapMany(categoriesMap -> Flux.fromIterable(BY_CATEGORY_SLUGS)
            .flatMap(slug -> {
              var category = categoriesMap.get(slug);
              return newsRepository.findTopByCategoryId(category.getId(), 8)
                  .map(news -> CategoryFeedItemDto.builder()
                      .category(category.getDescription())
                      .news(news.stream().map(this::toDto).toList())
                      .build());
            }))
        .collectList();

    Mono<List<ColumnistResponseDto>> columnists = columnistRepository.findLatestColumnists()
        .map(list -> list.stream().map(this::toColumnistDto).toList());

    Mono<DigitalWeeklyResponseDto> digitalWeekly = digitalWeeklyRepository.findLatest()
        .map(this::toDigitalWeeklyDto)
        .defaultIfEmpty(null);

    return Mono.zip(carousel, peruDaily, byCategory, columnists, digitalWeekly)
        .map(tuple -> FeedResponseDto.builder()
            .inCarousel(tuple.getT1())
            .peruDailyNews(tuple.getT2())
            .byCategory(tuple.getT3())
            .columnists(tuple.getT4())
            .digitalWeekly(tuple.getT5())
            .build()
        );
  }

  private NewsResponseDto toDto(News news) {
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

  private DigitalWeeklyResponseDto toDigitalWeeklyDto(DigitalWeekly digitalWeekly) {
    return DigitalWeeklyResponseDto.builder()
        .id(digitalWeekly.getId())
        .pdfUrl(digitalWeekly.getPdfUrl())
        .frontPageImageUrl(digitalWeekly.getFrontPageImageUrl())
        .descripcion(digitalWeekly.getDescripcion())
        .isActive(digitalWeekly.getIsActive())
        .createdAt(digitalWeekly.getCreatedAt())
        .updatedAt(digitalWeekly.getUpdatedAt())
        .isPremium(digitalWeekly.getIsPremium())
        .url(digitalWeekly.getUrl())
        .build();
  }

  private ColumnistResponseDto toColumnistDto(Columnist columnist) {
    return ColumnistResponseDto.builder()
        .id(columnist.getId())
        .content(columnist.getContent())
        .author(columnist.getAuthor())
        .title(columnist.getTitle())
        .headline(columnist.getHeadline())
        .authorImageUrl(columnist.getAuthorImageUrl())
        .isActive(columnist.getIsActive())
        .createdAt(columnist.getCreatedAt())
        .updatedAt(columnist.getUpdatedAt())
        .build();
  }
}
