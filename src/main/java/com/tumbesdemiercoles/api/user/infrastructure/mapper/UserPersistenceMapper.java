package com.tumbesdemiercoles.api.user.infrastructure.mapper;

import com.tumbesdemiercoles.api.user.domain.model.User;
import com.tumbesdemiercoles.api.user.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

  @Mapping(source = "userEmail", target = "email")
  @Mapping(source = "userImageUrl", target = "imageUrl")
  User toDomain(UserEntity entity);

  @Mapping(source = "email", target = "userEmail")
  @Mapping(source = "imageUrl", target = "userImageUrl")
  @Mapping(target = "createdAt", ignore = true) // Esto lo maneja Spring
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "statusRegistry", source = "statusRegistry") // Aseguramos el mapeo
  @Mapping(target = "statusUpdatedAt", source = "statusUpdatedAt")
  UserEntity toEntity(User domain);

}
