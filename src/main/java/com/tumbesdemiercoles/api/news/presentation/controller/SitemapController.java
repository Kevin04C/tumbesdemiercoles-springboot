package com.tumbesdemiercoles.api.news.presentation.controller;

import com.tumbesdemiercoles.api.news.application.dto.SitemapGeneralDto;
import com.tumbesdemiercoles.api.news.application.dto.SitemapNewsDto;
import com.tumbesdemiercoles.api.news.application.ports.in.GetSitemapUseCase;
import com.tumbesdemiercoles.api.news.presentation.api.SitemapControllerApi;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SitemapController implements SitemapControllerApi {

  private final GetSitemapUseCase getSitemapUseCase;

  @Override
  public Mono<ApiResponse<List<SitemapGeneralDto>>> getGeneralSitemap() {
    return getSitemapUseCase.getGeneral()
        .collectList()
        .map(ApiResponse::success);
  }

  @Override
  public Mono<ApiResponse<List<SitemapNewsDto>>> getNewsSitemap() {
    return getSitemapUseCase.getNews()
        .map(ApiResponse::success);
  }
}
