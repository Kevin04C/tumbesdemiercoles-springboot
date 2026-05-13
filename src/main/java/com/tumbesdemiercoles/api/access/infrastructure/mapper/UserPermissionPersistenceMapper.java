package com.tumbesdemiercoles.api.access.infrastructure.mapper;

import com.tumbesdemiercoles.api.access.domain.model.UserPermission;
import com.tumbesdemiercoles.api.access.infrastructure.entity.UserPermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserPermissionPersistenceMapper {

  UserPermission toDomain(UserPermissionEntity entity);

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "statusRegistry", source = "statusRegistry")
  @Mapping(target = "statusUpdatedAt", source = "statusUpdatedAt")
  UserPermissionEntity toEntity(UserPermission domain);

  @Mapping(target = "updatedAt", ignore = true)
  UserPermissionEntity toEntityUpdate(UserPermission domain);

}
