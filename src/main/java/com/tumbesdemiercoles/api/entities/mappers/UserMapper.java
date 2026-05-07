package com.tumbesdemiercoles.api.entities.mappers;

import com.tumbesdemiercoles.api.controllers.dto.UserRequest;
import com.tumbesdemiercoles.api.controllers.dto.UserResponse;
import com.tumbesdemiercoles.api.entities.IgnoreAuditingFields;
import com.tumbesdemiercoles.api.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper para convertir entre User (Entity) y los DTOs autogenerados de OpenAPI.
 */
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper {

  // ==========================================================
  // 1. DE ENTIDAD HACIA AFUERA (Lectura)
  // ==========================================================

  // Convierte el Entity al DTO de respuesta.
  // Nota: Como tu OpenAPI yaml no debería tener el campo password en el UserResponse,
  // MapStruct no intentará mapearlo, por lo que es seguro automáticamente.
  UserResponse toResponse(User user);


  // ==========================================================
  // 2. DE AFUERA HACIA ENTIDAD (Creación)
  // ==========================================================

  @IgnoreAuditingFields
  @Mapping(target = "userId", ignore = true) // Generado por la base de datos (UUID)
  @Mapping(target = "emailVerified", ignore = true) // Lógica de negocio lo pone en false inicialmente
  @Mapping(target = "passwordHash", source = "password") // Asumiendo que tu UserRequest trae el campo 'password'
  User toEntity(UserRequest userRequest);


  // ==========================================================
  // 3. ACTUALIZACIÓN DE ENTIDAD EXISTENTE (Modificación)
  // ==========================================================

  @IgnoreAuditingFields
  @Mapping(target = "userId", ignore = true)
  @Mapping(target = "emailVerified", ignore = true) // No se verifica con un update normal
  @Mapping(target = "passwordHash", ignore = true) // La contraseña suele cambiarse en otro endpoint específico
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateUserFromRequest(UserRequest userRequest, @MappingTarget User user);

}