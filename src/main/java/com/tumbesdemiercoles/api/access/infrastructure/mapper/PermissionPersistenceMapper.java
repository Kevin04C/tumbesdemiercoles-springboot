package com.tumbesdemiercoles.api.access.infrastructure.mapper;

import com.tumbesdemiercoles.api.access.domain.model.Permission;
import com.tumbesdemiercoles.api.access.infrastructure.entity.PermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionPersistenceMapper {

  @Mapping(source = "permissionName", target = "name")
  @Mapping(source = "permissionDescription", target = "description")
  Permission toDomain(PermissionEntity entity);

  @Mapping(source = "name", target = "permissionName")
  @Mapping(source = "description", target = "permissionDescription")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "statusRegistry", source = "statusRegistry")
  @Mapping(target = "statusUpdatedAt", source = "statusUpdatedAt")
  PermissionEntity toEntity(Permission domain);

  @Mapping(source = "name", target = "permissionName")
  @Mapping(source = "description", target = "permissionDescription")
  @Mapping(target = "updatedAt", ignore = true)
  PermissionEntity toEntityUpdate(Permission domain);

}
