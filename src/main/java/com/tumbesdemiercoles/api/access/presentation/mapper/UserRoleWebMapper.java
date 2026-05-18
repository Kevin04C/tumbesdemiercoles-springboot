package com.tumbesdemiercoles.api.access.presentation.mapper;

import com.tumbesdemiercoles.api.access.domain.model.UserRole;
import com.tumbesdemiercoles.api.access.presentation.dto.response.UserRoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRoleWebMapper {

  UserRoleResponse toResponse(UserRole domain);

}
