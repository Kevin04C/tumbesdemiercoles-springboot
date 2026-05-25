package com.tumbesdemiercoles.api.news.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {

  @NotBlank(message = "El contenido no puede estar vacío")
  private String content;

  private Boolean isCarousel;

  private String headline;

  @NotBlank(message = "El slug no puede estar vacío")
  private String slug;

  private Boolean isPremium;

  @NotNull(message = "La categoría es obligatoria")
  private UUID categoryId;

  private String title;

  private Boolean isActive;

  private String imageUrl;

  private Boolean isPeruDailyNews;

  private Boolean isLatestNews;
}
