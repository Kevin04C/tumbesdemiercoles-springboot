package com.tumbesdemiercoles.api.news.application.ports.in;

import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface GetRelatedNewsUseCase {
  Mono<List<NewsResponseDto>> getRelated(UUID newsId, int limit);
}
