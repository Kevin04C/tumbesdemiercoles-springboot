package com.tumbesdemiercoles.api.category.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para operaciones de categoría.
 * Transporta datos desde la capa de presentación hacia los casos de uso.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {
  private String description;
  private Boolean isActive;
  private String slug;
}
