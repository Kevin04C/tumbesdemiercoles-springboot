package com.tumbesdemiercoles.api.access.infrastructure.mapper;

import com.tumbesdemiercoles.api.access.application.dto.UserRoleWithNameDto;
import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import com.tumbesdemiercoles.api.access.infrastructure.entity.UserRoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRoleInfraMapper {

  UserRoleEntity toEntity(UserRole domain);

  @Mapping(target = "roleName", ignore = true)
  UserRole toDomain(UserRoleEntity entity);

  UserRole toDomain(UserRoleWithNameDto dto);

}
