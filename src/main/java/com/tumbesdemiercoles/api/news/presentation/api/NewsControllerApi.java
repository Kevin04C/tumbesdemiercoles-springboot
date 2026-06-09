package com.tumbesdemiercoles.api.news.presentation.api;

import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsByCategorySlugFilterRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsFilterRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.request.NewsUpdateRequest;
import com.tumbesdemiercoles.api.news.presentation.dto.response.NewsResponse;
import com.tumbesdemiercoles.api.news.presentation.dto.response.RelatedNewsResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@Tag(name = "News", description = "Endpoints para la publicación, consulta y administración de artículos de noticias")
@RequestMapping("/api/v1/news")
public interface NewsControllerApi {

  @Operation(summary = "Crear artículo de noticia", description = "Publica una nueva noticia en el sistema con su contenido, categoría, imagen e indicación de si es premium.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ApiResponse<NewsResponse>> createNews(@Valid @RequestBody NewsRequest request);

  @Operation(summary = "Listar noticias", description = "Retorna una página de noticias filtradas por parámetros de búsqueda (categoría, estado, premium, etc.).")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<NewsResponse>>> findNewsList(@Valid NewsFilterRequest filter);

  @Operation(summary = "Obtener noticia por ID", description = "Busca y retorna la información detallada de una noticia determinada mediante su ID único.")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<NewsResponse>> getNewsById(@Parameter(description = "ID único del artículo de noticia", required = true) @PathVariable UUID id);

  @Operation(summary = "Obtener noticia por slug", description = "Busca y retorna la información detallada de una noticia determinada mediante su slug.")
  @GetMapping("/slug/{slug}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<NewsResponse>> getNewsBySlug(
      @Parameter(description = "Slug único del artículo de noticia", required = true) @PathVariable String slug);

  @Operation(summary = "Obtener noticias por slug de categoría", description = "Retorna una página de noticias filtradas por el slug de una categoría.")
  @GetMapping("/by-category/{categorySlug}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<NewsResponse>>> getNewsByCategorySlug(
      @Parameter(description = "Slug de la categoría", required = true) @PathVariable String categorySlug,
      @Valid NewsByCategorySlugFilterRequest filter);

  @Operation(summary = "Obtener últimas noticias", description = "Retorna una lista de las últimas noticias destacadas como 'latest news'.")
  @GetMapping("/latest")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<NewsResponse>>> getLatestNews(
      @Parameter(description = "Cantidad máxima de noticias a retornar", required = false)
      @RequestParam(defaultValue = "10") int limit);

  @Operation(summary = "Obtener noticias relacionadas", description = "Retorna una lista de noticias similares a la noticia especificada, basadas en categoría y similitud textual.")
  @GetMapping("/{id}/related")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<List<RelatedNewsResponse>>> getRelatedNews(
      @Parameter(description = "ID único del artículo de noticia", required = true) @PathVariable UUID id);

  @Operation(summary = "Actualizar noticia", description = "Modifica los campos editables de un artículo de noticia identificado por su ID.")
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<NewsResponse>> updateNews(
      @Parameter(description = "ID del artículo a actualizar", required = true) @PathVariable UUID id,
      @Valid @RequestBody NewsUpdateRequest request);

  @Operation(summary = "Eliminar noticia", description = "Realiza la eliminación de una noticia por su ID.")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Mono<ApiResponse<Void>> deleteNews(
      @Parameter(description = "ID del artículo a eliminar", required = true) @PathVariable UUID id);
}

