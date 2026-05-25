package com.tumbesdemiercoles.api.category.application.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida para operaciones de categoría.
 * Transporta datos desde los casos de uso hacia la capa de presentación.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

  private UUID id;
  private String description;
  private Boolean isActive;
  private UUID categoryId;
  private String slug;
  private List<CategoryResponseDto> children;

}
