package com.tumbesdemiercoles.api.category.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

/**
 * Entidad de dominio Category.
 * POJO puro sin anotaciones de frameworks (ni Spring Data, ni JPA).
 * Representa el concepto de negocio "Categoría" en su forma más pura.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

  private UUID id;
  private String description;
  private Boolean isActive;
  private UUID categoryId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String statusRegistry;
  private LocalDateTime statusUpdatedAt;

}
