package com.tumbesdemiercoles.api.access.infrastructure.mapper;

import com.tumbesdemiercoles.api.access.domain.model.RolePermission;
import com.tumbesdemiercoles.api.access.infrastructure.entity.RolePermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolePermissionPersistenceMapper {

  RolePermission toDomain(RolePermissionEntity entity);

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "statusRegistry", source = "statusRegistry")
  @Mapping(target = "statusUpdatedAt", source = "statusUpdatedAt")
  RolePermissionEntity toEntity(RolePermission domain);

  @Mapping(target = "updatedAt", ignore = true)
  RolePermissionEntity toEntityUpdate(RolePermission domain);

}
