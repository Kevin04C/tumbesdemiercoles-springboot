package com.tumbesdemiercoles.api.news.application.ports.in;

import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import com.tumbesdemiercoles.api.news.domain.model.NewsFilter;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface GetNewsUseCase {
  Mono<NewsResponseDto> getById(UUID id);
  Mono<PageResponseDto<NewsResponseDto>> findNewsList(NewsFilter filter);
}
