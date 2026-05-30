package com.tumbesdemiercoles.api.news.application.usecase;

import com.tumbesdemiercoles.api.news.application.dto.RelatedNewsResponseDto;
import com.tumbesdemiercoles.api.news.application.ports.in.GetRelatedNewsUseCase;
import com.tumbesdemiercoles.api.news.domain.model.News;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import com.tumbesdemiercoles.api.news.domain.service.NewsSimilarityScorer;
import com.tumbesdemiercoles.api.shared.exception.ResourceNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetRelatedNewsUseCaseImpl implements GetRelatedNewsUseCase {

  private static final int SAME_CATEGORY_LIMIT = 30;
  private static final int OTHER_CATEGORY_LIMIT = 20;

  private final NewsRepository newsRepository;
  private final NewsSimilarityScorer similarityScorer;

  @Override
  public Mono<List<RelatedNewsResponseDto>> getRelated(UUID newsId, int limit) {
    return newsRepository.findById(newsId)
        .switchIfEmpty(Mono.error(ResourceNotFoundException.forEntity("News", newsId)))
        .flatMap(sourceNews -> newsRepository.findRelatedCandidates(
                newsId,
                sourceNews.getCategoryId(),
                SAME_CATEGORY_LIMIT,
                OTHER_CATEGORY_LIMIT
            )
            .map(candidates -> scoreAndSort(sourceNews, candidates, limit)));
  }

  private List<RelatedNewsResponseDto> scoreAndSort(News source, List<News> candidates, int limit) {
    return candidates.stream()
        .map(candidate -> new ScoredCandidate(candidate, similarityScorer.score(source, candidate)))
        .filter(scored -> scored.score > 0)
        .sorted(Comparator.comparingDouble(ScoredCandidate::score).reversed())
        .limit(limit)
        .map(scored -> toResponse(scored.candidate))
        .toList();
  }

  private RelatedNewsResponseDto toResponse(News news) {
    return RelatedNewsResponseDto.builder()
        .id(news.getId())
        .title(news.getTitle())
        .slug(news.getSlug())
        .headline(news.getHeadline())
        .imageUrl(news.getImageUrl())
        .createdAt(news.getCreatedAt())
        .build();
  }

  private record ScoredCandidate(News candidate, double score) {}
}
