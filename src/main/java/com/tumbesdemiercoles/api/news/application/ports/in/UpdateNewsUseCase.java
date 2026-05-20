package com.tumbesdemiercoles.api.news.application.ports.in;

import com.tumbesdemiercoles.api.news.application.dto.NewsRequestDto;
import com.tumbesdemiercoles.api.news.application.dto.NewsResponseDto;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdateNewsUseCase {
  Mono<NewsResponseDto> execute(UUID id, NewsRequestDto dto);
}
