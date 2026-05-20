# RESUMEN DE CAMBIOS: RBAC con Excepciones (Overrides)

Este documento detalla la estructura y la lógica implementada para gestionar las excepciones de permisos por usuario de acuerdo con el patrón **RBAC con Excepciones**.

## Lógica del Delta de Permisos (Escenarios A, B y C)

El caso de uso `ManageUserPermissionExceptionsUseCaseImpl` calcula dinámicamente las diferencias en base a:
1. Los permisos por defecto definidos por los roles activos del usuario.
2. Los overrides existentes en la tabla `user_permission`.
3. La lista de overrides deseados proporcionados en la petición.

Se aplican las siguientes reglas de negocio reactivas:
- **Escenario A (Agregado)**: Si el rol no tiene el permiso por defecto y se asigna con `isActive = true`, se inserta o actualiza el registro en `user_permission` con `isActive = true` y estado `ACTIVE`.
- **Escenario B (Revocación)**: Si el rol tiene el permiso por defecto y se quita (`isActive = false`), se inserta o actualiza el registro en `user_permission` con `isActive = false` y estado `ACTIVE`.
- **Escenario C (Limpieza/Igualdad)**: Si el estado deseado es igual al estado por defecto del rol, se realiza una **eliminación física (DELETE)** en la tabla `user_permission` para mantener la base de datos limpia de registros redundantes.

---

## Estructura de Archivos Creados y Modificados

### 1. Dominio (Domain Layer)
- **Modificado**: [UserPermission.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/domain/model/UserPermission.java)
  - Añadido el método `updateActive(Boolean isActive)` para cambiar el estado activo del override.
- **Modificado**: [UserPermissionRepository.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/domain/repository/UserPermissionRepository.java)
  - Añadidas las firmas de contrato:
    - `Mono<Void> deleteByUserIdAndPermissionId(UUID userId, UUID permissionId);`
    - `Flux<Permission> findEffectivePermissionsByUserId(UUID userId);`

### 2. Aplicación (Application Layer)
- **Nuevo**: [ManageUserPermissionExceptionsUseCase.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/application/ports/in/ManageUserPermissionExceptionsUseCase.java) (Puerto de entrada)
- **Nuevo**: [GetEffectivePermissionsUseCase.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/application/ports/in/GetEffectivePermissionsUseCase.java) (Puerto de entrada)
- **Nuevo**: [UpdateUserPermissionExceptionsRequestDto.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/application/dto/UpdateUserPermissionExceptionsRequestDto.java) (DTO de entrada)
- **Nuevo**: [ManageUserPermissionExceptionsUseCaseImpl.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/application/usecase/ManageUserPermissionExceptionsUseCaseImpl.java) (Caso de uso principal)
- **Nuevo**: [GetEffectivePermissionsUseCaseImpl.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/application/usecase/GetEffectivePermissionsUseCaseImpl.java) (Caso de uso de permisos efectivos)

### 3. Infraestructura (Infrastructure Layer)
- **Modificado**: [UserPermissionR2dbcRepository.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/infrastructure/repository/UserPermissionR2dbcRepository.java)
  - Añadida anotación `@Modifying` y consulta nativa para la eliminación física.
  - Añadida consulta nativa (`@Query`) para fusionar y obtener de forma reactiva los permisos efectivos del usuario basándose en sus roles activos y overrides de usuario.
- **Modificado**: [UserPermissionRepositoryAdapter.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/infrastructure/adapter/UserPermissionRepositoryAdapter.java)
  - Implementación de los nuevos métodos y adaptaciones del Mapper para persistencia.

### 4. Presentación (Presentation Layer)
- **Nuevo**: [UpdateUserPermissionExceptionsRequest.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/presentation/dto/request/UpdateUserPermissionExceptionsRequest.java) (DTO de petición HTTP)
- **Modificado**: [UserPermissionControllerApi.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/presentation/api/UserPermissionControllerApi.java)
  - Declarados los nuevos endpoints:
    - `PUT /api/v1/users/{userId}/permissions/exceptions`
    - `GET /api/v1/users/{userId}/permissions/effective`
- **Modificado**: [UserPermissionController.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/presentation/controller/UserPermissionController.java)
  - Inyección de nuevos casos de uso y lógica de mapeo web.

---

## Validación de Compilación

La compilación y la generación de mappers de MapStruct se validaron exitosamente:
```powershell
.\mvnw.cmd clean test-compile
```
Estado del build: **BUILD SUCCESS** sin errores.
