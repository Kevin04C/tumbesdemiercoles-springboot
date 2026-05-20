package com.tumbesdemiercoles.api.columnist.infrastructure.mapper;

import com.tumbesdemiercoles.api.columnist.domain.model.Columnist;
import com.tumbesdemiercoles.api.columnist.infrastructure.entity.ColumnistEntity;
import org.springframework.stereotype.Component;

@Component
public class ColumnistPersistenceMapper {

  public Columnist toDomain(ColumnistEntity entity) {
    return Columnist.builder()
        .id(entity.getId())
        .content(entity.getContent())
        .author(entity.getAuthor())
        .title(entity.getTitle())
        .headline(entity.getHeadline())
        .authorImageUrl(entity.getAuthorImageUrl())
        .isActive(entity.getIsActive())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .statusRegistry(entity.getStatusRegistry())
        .statusUpdatedAt(entity.getStatusUpdatedAt())
        .build();
  }

  public ColumnistEntity toEntity(Columnist domain) {
    return ColumnistEntity.builder()
        .id(domain.getId())
        .content(domain.getContent())
        .author(domain.getAuthor())
        .title(domain.getTitle())
        .headline(domain.getHeadline())
        .authorImageUrl(domain.getAuthorImageUrl())
        .isActive(domain.getIsActive())
        .build();
  }
}
