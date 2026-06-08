package com.tumbesdemiercoles.api.news.application.ports.in;

import com.tumbesdemiercoles.api.news.application.dto.SitemapGeneralDto;
import com.tumbesdemiercoles.api.news.application.dto.SitemapNewsDto;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetSitemapUseCase {
  Flux<SitemapGeneralDto> getGeneral();
  Mono<List<SitemapNewsDto>> getNews();
}
