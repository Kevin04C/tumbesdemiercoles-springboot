# RESUMEN DE LIMPIEZA: Eliminación de Dead Code (Módulo Access)

Como parte de la transición hacia el modelo **RBAC con Excepciones**, se realizó una limpieza arquitectónica exhaustiva del código obsoleto ("Dead Code") relacionado con la gestión directa (asignación y revocación) de permisos por usuario, ya que este enfoque ha sido sustituido por completo por los overrides y permisos efectivos.

## Archivos Eliminados (Dead Code)

Los siguientes componentes fueron **eliminados** por completo del proyecto:

### Capa Application
- **Puertos de Entrada (Interfaces):**
  - `src/main/java/com/tumbesdemiercoles/api/access/application/ports/in/AssignUserPermissionUseCase.java`
  - `src/main/java/com/tumbesdemiercoles/api/access/application/ports/in/RevokeUserPermissionUseCase.java`
  - `src/main/java/com/tumbesdemiercoles/api/access/application/ports/in/GetUserPermissionUseCase.java`
- **Casos de Uso (Implementaciones):**
  - `src/main/java/com/tumbesdemiercoles/api/access/application/usecase/AssignUserPermissionUseCaseImpl.java`
  - `src/main/java/com/tumbesdemiercoles/api/access/application/usecase/RevokeUserPermissionUseCaseImpl.java`
  - `src/main/java/com/tumbesdemiercoles/api/access/application/usecase/GetUserPermissionUseCaseImpl.java`
- **Data Transfer Objects (DTO):**
  - `src/main/java/com/tumbesdemiercoles/api/access/application/dto/AssignUserPermissionRequestDto.java`

### Capa Presentation
- **Data Transfer Objects (DTO):**
  - `src/main/java/com/tumbesdemiercoles/api/access/presentation/dto/request/AssignUserPermissionRequest.java`

---

## Archivos Modificados (Refactorización de Limpieza)

Los siguientes archivos fueron **modificados** para remover referencias a los métodos e importaciones eliminadas:

### Capa Presentation
- **[UserPermissionControllerApi.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/presentation/api/UserPermissionControllerApi.java)**
  - Se eliminaron las firmas de los endpoints `@PostMapping` (asignar), `@DeleteMapping` (revocar) y `@GetMapping` (obtener asignaciones directas).
  - La interfaz quedó exclusivamente con los endpoints para el patrón de excepciones (`updateExceptions` y `getEffectivePermissions`).
- **[UserPermissionController.java](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/src/main/java/com/tumbesdemiercoles/api/access/presentation/controller/UserPermissionController.java)**
  - Se eliminaron las implementaciones de los métodos HTTP correspondientes.
  - Se limpió el constructor eliminando las inyecciones de dependencia de los 3 Casos de Uso obsoletos que fueron borrados.
  - Se removieron los imports sin uso.

---

## Actualización de Colección de Pruebas (Bruno/Postman)

En el directorio de pruebas de API (`api-tests/Tumbes de Miercoles Api/ACCESS/USER_PERMISSION`), se realizaron las siguientes acciones:
- **Pruebas Eliminadas:**
  - `ASSIGN_USER_PERMISSIONS.yml`
  - `REVOKE_USER_PERMISSION.yml`
  - `LIST_PERMISSIONS_BY_USER.yml`
- **Pruebas Creadas:**
  - `UPDATE_USER_PERMISSION_EXCEPTIONS.yml` (PUT para actualizar overrides)
  - `GET_EFFECTIVE_PERMISSIONS.yml` (GET para traer permisos fusionados)

**Restricciones respetadas:** No se realizaron modificaciones en la capa de Infraestructura (`infrastructure`) ni en la de Dominio (`domain`), manteniendo intacto el motor actual de Overrides implementado previamente.
