package com.tumbesdemiercoles.api.digitalweekly.infrastructure.mapper;

import com.tumbesdemiercoles.api.digitalweekly.domain.model.DigitalWeekly;
import com.tumbesdemiercoles.api.digitalweekly.infrastructure.entity.DigitalWeeklyEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper de persistencia: convierte entre DigitalWeekly (dominio) y DigitalWeeklyEntity (infraestructura).
 */
@Component
public class DigitalWeeklyPersistenceMapper {

  public DigitalWeekly toDomain(DigitalWeeklyEntity entity) {
    return DigitalWeekly.builder()
        .id(entity.getId())
        .pdfUrl(entity.getPdfUrl())
        .frontPageImageUrl(entity.getFrontPageImageUrl())
        .descripcion(entity.getDescripcion())
        .isActive(entity.getIsActive())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .statusRegistry(entity.getStatusRegistry())
        .statusUpdatedAt(entity.getStatusUpdatedAt())
        .isPremium(entity.getIsPremium())
        .url(entity.getUrl())
        .build();
  }

  public DigitalWeeklyEntity toEntity(DigitalWeekly domain) {
    return DigitalWeeklyEntity.builder()
        .id(domain.getId())
        .pdfUrl(domain.getPdfUrl())
        .frontPageImageUrl(domain.getFrontPageImageUrl())
        .descripcion(domain.getDescripcion())
        .isActive(domain.getIsActive())
        .isPremium(domain.getIsPremium())
        .url(domain.getUrl())
        .build();
  }
}
