package com.tumbesdemiercoles.api.access.infrastructure.mapper;

import com.tumbesdemiercoles.api.access.domain.model.Role;
import com.tumbesdemiercoles.api.access.infrastructure.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolePersistenceMapper {

  @Mapping(source = "roleName", target = "name")
  Role toDomain(RoleEntity entity);

  @Mapping(source = "name", target = "roleName")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "statusRegistry", source = "statusRegistry")
  @Mapping(target = "statusUpdatedAt", source = "statusUpdatedAt")
  RoleEntity toEntity(Role domain);

  @Mapping(source = "name", target = "roleName")
  @Mapping(target = "updatedAt", ignore = true)
  RoleEntity toEntityUpdate(Role domain);

}
