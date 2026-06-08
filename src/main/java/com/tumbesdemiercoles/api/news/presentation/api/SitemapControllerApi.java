package com.tumbesdemiercoles.api.news.presentation.api;

import com.tumbesdemiercoles.api.news.application.dto.SitemapGeneralDto;
import com.tumbesdemiercoles.api.news.application.dto.SitemapNewsDto;
import com.tumbesdemiercoles.api.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "Sitemap", description = "Endpoints públicos para generación de sitemaps XML")
@RequestMapping("/api/v1/sitemap")
public interface SitemapControllerApi {

  @Operation(summary = "Sitemap general", description = "Retorna slug y updatedAt de todas las noticias activas para el sitemap.xml")
  @GetMapping("/general")
  Mono<ApiResponse<List<SitemapGeneralDto>>> getGeneralSitemap();

  @Operation(summary = "Sitemap Google News", description = "Retorna slug, title, createdAt e imageUrl de noticias activas de las últimas 48 horas para el sitemap-news.xml")
  @GetMapping("/news")
  Mono<ApiResponse<List<SitemapNewsDto>>> getNewsSitemap();
}
