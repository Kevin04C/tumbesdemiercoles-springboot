package com.tumbesdemiercoles.api.common.presentation.api;

import com.tumbesdemiercoles.api.common.presentation.dto.response.FeedResponse;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "Feed", description = "Endpoints para la consulta del feed general de noticias y columnas")
@RequestMapping("/api/v1/feed")
public interface FeedControllerApi {

  @Operation(summary = "Obtener feed general", description = "Retorna el conjunto consolidado de noticias (carrusel, destacadas, nacionales, últimas noticias) y columnas de opinión más recientes para el inicio de la app.")
  @GetMapping
  Mono<ApiResponse<FeedResponse>> getFeed();
}

