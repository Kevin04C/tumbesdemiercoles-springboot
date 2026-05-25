package com.tumbesdemiercoles.api.news.presentation.controller;

import com.tumbesdemiercoles.api.news.application.ports.in.CreateNewsUseCase;
import com.tumbesdemiercoles.api.news.application.ports.in.DeleteNewsUseCase;
import com.tumbesdemiercoles.api.news.application.ports.in.GetNewsUseCase;
import com.tumbesdemiercoles.api.news.application.ports.in.UpdateNewsUseCase;
import com.tumbesdemiercoles.api.news.presentation.api.NewsControllerApi;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsFilterRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsUpdateRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.response.NewsResponse;
import com.tumbesdemiercoles.api.news.presentation.mapper.NewsWebMapper;
import com.tumbesdemiercoles.api.shared.application.dto.PageResponseDto;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class NewsController implements NewsControllerApi {

  private final CreateNewsUseCase createNewsUseCase;
  private final UpdateNewsUseCase updateNewsUseCase;
  private final DeleteNewsUseCase deleteNewsUseCase;
  private final GetNewsUseCase getNewsUseCase;
  private final NewsWebMapper webMapper;

  @Override
  public Mono<ApiResponse<NewsResponse>> createNews(NewsRequest request) {
    return createNewsUseCase.execute(webMapper.toCreateDto(request))
        .map(webMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Noticia creada correctamente"));
  }

  @Override
  public Mono<ApiResponse<PageResponseDto<NewsResponse>>> findNewsList(NewsFilterRequest filter) {
    return getNewsUseCase.findNewsList(webMapper.toFilter(filter))
        .transform(webMapper::toPageResponse)
        .map(pageDto -> ApiResponse.success(pageDto));
  }


  @Override
  public Mono<ApiResponse<NewsResponse>> getNewsById(UUID id) {
    return getNewsUseCase.getById(id)
        .map(webMapper::toResponse)
        .map(ApiResponse::success);
  }

  @Override
  public Mono<ApiResponse<NewsResponse>> updateNews(UUID id, NewsUpdateRequest request) {
    return updateNewsUseCase.execute(id, webMapper.toUpdateDto(request))
        .map(webMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Noticia actualizada correctamente"));
  }

  @Override
  public Mono<ApiResponse<Void>> deleteNews(UUID id) {
    return deleteNewsUseCase.execute(id)
        .thenReturn(ApiResponse.success(null, "Noticia eliminada correctamente"));
  }
}
