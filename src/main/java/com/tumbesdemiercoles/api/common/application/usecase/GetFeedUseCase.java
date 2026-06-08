package com.tumbesdemiercoles.api.common.application.usecase;

import com.tumbesdemiercoles.api.category.domain.model.Category;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GetFeedUseCase {

  private static final Logger log = LoggerFactory.getLogger(GetFeedUseCase.class);

  private static final Map<String, Integer> CATEGORY_ORDER = Map.of(
      CategoriesSlugConst.TUMBES, 1,
      CategoriesSlugConst.ZARUMILLA, 2,
      CategoriesSlugConst.CONTRALMIRANTE_VILLAR, 3,
      CategoriesSlugConst.POLITICA, 4,
      CategoriesSlugConst.MUNDO, 5,
      CategoriesSlugConst.REGION, 6,
      CategoriesSlugConst.DEPORTIVAS, 7,
      CategoriesSlugConst.POLICIALES, 8,
      CategoriesSlugConst.PERU, 10
  );

  private final NewsRepository newsRepository;
  private final CategoryRepository categoryRepository;
  private final ColumnistRepository columnistRepository;
  private final DigitalWeeklyRepository digitalWeeklyRepository;

  private static final List<String> BY_CATEGORY_SLUGS = List.of(
      CategoriesSlugConst.TUMBES,
      CategoriesSlugConst.ZARUMILLA,
      CategoriesSlugConst.CONTRALMIRANTE_VILLAR,
      CategoriesSlugConst.POLITICA,
      CategoriesSlugConst.MUNDO,
      CategoriesSlugConst.REGION,
      CategoriesSlugConst.DEPORTIVAS,
      CategoriesSlugConst.POLICIALES,
      CategoriesSlugConst.PERU
  );

  public Mono<FeedResponseDto> execute() {
    Mono<List<NewsResponseDto>> carousel = newsRepository.findTopByIsCarousel(10)
        .map(list -> list.stream().map(this::toDto).toList());

    Mono<List<NewsResponseDto>> peruDaily = newsRepository.findTopByIsPeruDailyNews(3)
        .map(list -> list.stream().map(this::toDto).toList());

    Mono<List<CategoryFeedItemDto>> byCategory = categoryRepository.findBySlugIn(BY_CATEGORY_SLUGS)
        .flatMap(categoriesMap -> {
          List<UUID> categoryIds = BY_CATEGORY_SLUGS.stream()
              .map(categoriesMap::get)
              .filter(Objects::nonNull)
              .map(Category::getId)
              .toList();

          log.debug("Fetching top {} news for category IDs: {}", 8, categoryIds);

          return newsRepository.findTopByCategoryIds(categoryIds, 8)
              .collectMultimap(News::getCategoryId, Function.identity())
              .map(groupedNews -> BY_CATEGORY_SLUGS.stream()
                  .map(categoriesMap::get)
                  .filter(Objects::nonNull)
                  .map(category -> CategoryFeedItemDto.builder()
                      .category(category.getDescription())
                      .slug(category.getSlug())
                      .order(CATEGORY_ORDER.getOrDefault(category.getSlug(), 99))
                      .news(groupedNews.getOrDefault(category.getId(), List.of())
                          .stream()
                          .map(this::toDto)
                          .toList())
                      .build())
                  .toList());
        });

    Mono<List<ColumnistResponseDto>> columnists = columnistRepository.findLatestColumnists()
        .map(list -> list.stream().map(this::toColumnistDto).toList());

    Mono<Optional<DigitalWeeklyResponseDto>> digitalWeekly = digitalWeeklyRepository.findLatest()
        .map(this::toDigitalWeeklyDto)
        .map(Optional::of)
        .defaultIfEmpty(Optional.empty());

    return Mono.zip(carousel, peruDaily, byCategory, columnists, digitalWeekly)
        .map(tuple -> FeedResponseDto.builder()
            .inCarousel(tuple.getT1())
            .peruDailyNews(tuple.getT2())
            .byCategory(tuple.getT3())
            .columnists(tuple.getT4())
            .digitalWeekly(tuple.getT5().orElse(null))
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
