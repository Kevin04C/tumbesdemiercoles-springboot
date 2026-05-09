package com.tumbesdemiercoles.api.category.infrastructure.mapper;

import com.tumbesdemiercoles.api.category.domain.model.Category;
import com.tumbesdemiercoles.api.category.infrastructure.entity.CategoryEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper de persistencia: convierte entre Category (dominio) y CategoryEntity (infraestructura).
 */
@Component
public class CategoryPersistenceMapper {

  public Category toDomain(CategoryEntity entity) {
    return Category.builder()
        .id(entity.getId())
        .description(entity.getDescription())
        .isActive(entity.getIsActive())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .statusRegistry(entity.getStatusRegistry())
        .statusUpdatedAt(entity.getStatusUpdatedAt())
        .build();
  }

  public CategoryEntity toEntity(Category domain) {
    return CategoryEntity.builder()
        .id(domain.getId())
        .description(domain.getDescription())
        .isActive(domain.getIsActive())
        .build();
  }

}
