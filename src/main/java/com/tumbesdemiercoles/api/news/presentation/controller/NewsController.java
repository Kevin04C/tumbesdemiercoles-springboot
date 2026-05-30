package com.tumbesdemiercoles.api.news.presentation.controller;

import com.tumbesdemiercoles.api.news.application.ports.in.CreateNewsUseCase;
import com.tumbesdemiercoles.api.news.application.ports.in.DeleteNewsUseCase;
import com.tumbesdemiercoles.api.news.application.ports.in.GetNewsUseCase;
import com.tumbesdemiercoles.api.news.application.ports.in.GetRelatedNewsUseCase;
import com.tumbesdemiercoles.api.news.application.ports.in.UpdateNewsUseCase;
import com.tumbesdemiercoles.api.news.presentation.api.NewsControllerApi;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsByCategorySlugFilterRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsFilterRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsUpdateRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.response.NewsResponse;
import com.tumbesdemiercoles.api.news.presentation.dto.response.RelatedNewsResponse;
import com.tumbesdemiercoles.api.news.presentation.mapper.NewsWebMapper;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import java.util.List;
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
  private final GetRelatedNewsUseCase getRelatedNewsUseCase;
  private final NewsWebMapper webMapper;

  @Override
  public Mono<ApiResponse<NewsResponse>> createNews(NewsRequest request) {
    return createNewsUseCase.execute(webMapper.toCreateDto(request))
        .map(webMapper::toResponse)
        .map(response -> ApiResponse.success(response, "Noticia creada correctamente"));
  }

  @Override
  public Mono<ApiResponse<List<NewsResponse>>> findNewsList(NewsFilterRequest filter) {
    return getNewsUseCase.findNewsList(webMapper.toFilter(filter))
        .transform(webMapper::toPageResponse)
        .map(pageDto -> ApiResponse.success(pageDto, "Noticias encontradas"));
  }


  @Override
  public Mono<ApiResponse<NewsResponse>> getNewsById(UUID id) {
    return getNewsUseCase.getById(id)
        .map(webMapper::toResponse)
        .map(ApiResponse::success);
  }

  @Override
  public Mono<ApiResponse<NewsResponse>> getNewsBySlug(String slug) {
    return getNewsUseCase.getBySlug(slug)
        .map(webMapper::toResponse)
        .map(ApiResponse::success);
  }

  @Override
  public Mono<ApiResponse<List<NewsResponse>>> getNewsByCategorySlug(String categorySlug, NewsByCategorySlugFilterRequest filter) {
    return getNewsUseCase.getByCategorySlug(categorySlug, webMapper.toFilterByCategorySlug(filter))
        .transform(webMapper::toPageResponse)
        .map(pageDto -> ApiResponse.success(pageDto, "Noticias encontradas por categoría"));
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
        .thenReturn(ApiResponse.success((Void) null, "Noticia eliminada correctamente"));
  }

  @Override
  public Mono<ApiResponse<List<RelatedNewsResponse>>> getRelatedNews(UUID id) {
    return getRelatedNewsUseCase.getRelated(id, 10)
        .map(webMapper::toRelatedResponseList)
        .map(response -> ApiResponse.success(response, "Noticias relacionadas encontradas"));
  }
}
