package com.tumbesdemiercoles.api.news.application.ports.in;

import com.tumbesdemiercoles.api.news.application.dto.NewsRequestDto;
import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import reactor.core.publisher.Mono;

public interface CreateNewsUseCase {
  Mono<NewsResponseDto> execute(NewsRequestDto dto);
}
