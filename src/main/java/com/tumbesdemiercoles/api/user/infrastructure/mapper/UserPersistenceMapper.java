package com.tumbesdemiercoles.api.user.infrastructure.mapper;

import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.infrastructure.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper de persistencia: convierte entre User (dominio) y UserEntity (infraestructura).
 */
@Component
public class UserPersistenceMapper {

  public User toDomain(UserEntity entity) {
    return User.builder()
        .userId(entity.getUserId())
        .firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .email(entity.getUserEmail())
        .imageUrl(entity.getUserImageUrl())
        .emailVerified(entity.getEmailVerified())
        .passwordHash(entity.getPasswordHash())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .statusRegistry(entity.getStatusRegistry())
        .statusUpdatedAt(entity.getStatusUpdatedAt())
        .build();
  }

  public UserEntity toEntity(User domain) {
    return UserEntity.builder()
        .userId(domain.getUserId())
        .firstName(domain.getFirstName())
        .lastName(domain.getLastName())
        .userEmail(domain.getEmail())
        .userImageUrl(domain.getImageUrl())
        .emailVerified(domain.getEmailVerified())
        .passwordHash(domain.getPasswordHash())
        .build();
  }

}
