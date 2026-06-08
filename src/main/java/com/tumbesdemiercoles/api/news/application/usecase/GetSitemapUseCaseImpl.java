package com.tumbesdemiercoles.api.news.application.usecase;

import com.tumbesdemiercoles.api.news.application.dto.SitemapGeneralDto;
import com.tumbesdemiercoles.api.news.application.dto.SitemapNewsDto;
import com.tumbesdemiercoles.api.news.application.ports.in.GetSitemapUseCase;
import com.tumbesdemiercoles.api.news.domain.repository.NewsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetSitemapUseCaseImpl implements GetSitemapUseCase {

  private final NewsRepository newsRepository;

  @Override
  public Flux<SitemapGeneralDto> getGeneral() {
    return newsRepository.findSitemapGeneral();
  }

  @Override
  public Mono<List<SitemapNewsDto>> getNews() {
    return newsRepository.findSitemapNews()
        .collectList()
        .flatMap(list -> list.isEmpty()
            ? newsRepository.findLatestSitemapNews(10)
            : Mono.just(list));
  }
}
