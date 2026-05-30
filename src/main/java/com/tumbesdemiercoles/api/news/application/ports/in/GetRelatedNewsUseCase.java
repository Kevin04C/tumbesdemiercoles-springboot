package com.tumbesdemiercoles.api.news.application.ports.in;

import com.tumbesdemiercoles.api.news.application.dto.RelatedNewsResponseDto;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface GetRelatedNewsUseCase {
  Mono<List<RelatedNewsResponseDto>> getRelated(UUID newsId, int limit);
}
