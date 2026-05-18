package com.tumbesdemiercoles.api.access.infrastructure.mapper;

import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import com.tumbesdemiercoles.api.access.infrastructure.entity.UserRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRoleInfraMapper {

  UserRoleEntity toEntity(UserRole domain);

  UserRole toDomain(UserRoleEntity entity);

}
