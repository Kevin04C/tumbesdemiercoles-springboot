# 🛡️ Módulo `access` — Roles, Permisos y Tablas Intermedias

## Contexto

Un solo módulo `access` que gestiona las 4 tablas: `role`, `permission`, `role_permission` y `user_permission`. Sigue el patrón del módulo `user` (ports in/out, adapters, MapStruct, R2DBC reactivo). No depende de ningún otro módulo — si necesita verificar que un usuario existe, usa un **port out** (`UserExistencePort`) que el módulo `user` implementa en su `infrastructure/adapter/external/`.

---

## Estructura del Módulo

```
com.tumbesdemiercoles.api.access/
│
├── domain/
│   ├── model/
│   │   ├── Role.java
│   │   ├── Permission.java
│   │   ├── RolePermission.java
│   │   └── UserPermission.java
│   ├── repository/
│   │   ├── RoleRepository.java
│   │   ├── PermissionRepository.java
│   │   ├── RolePermissionRepository.java
│   │   └── UserPermissionRepository.java
│   └── exception/
│       ├── RoleNotFoundException.java
│       └── PermissionNotFoundException.java
│
├── application/
│   ├── dto/
│   │   ├── RoleRequestDto.java
│   │   ├── RoleResponseDto.java
│   │   ├── PermissionRequestDto.java
│   │   ├── PermissionResponseDto.java
│   │   ├── AssignRolePermissionRequestDto.java
│   │   ├── RolePermissionResponseDto.java
│   │   ├── AssignUserPermissionRequestDto.java
│   │   └── UserPermissionResponseDto.java
│   ├── ports/
│   │   ├── in/
│   │   │   ├── CreateRoleUseCase.java
│   │   │   ├── GetRoleUseCase.java
│   │   │   ├── UpdateRoleUseCase.java
│   │   │   ├── DeleteRoleUseCase.java
│   │   │   ├── CreatePermissionUseCase.java
│   │   │   ├── GetPermissionUseCase.java
│   │   │   ├── UpdatePermissionUseCase.java
│   │   │   ├── DeletePermissionUseCase.java
│   │   │   ├── AssignRolePermissionUseCase.java
│   │   │   ├── RevokeRolePermissionUseCase.java
│   │   │   ├── GetRolePermissionUseCase.java
│   │   │   ├── AssignUserPermissionUseCase.java
│   │   │   ├── RevokeUserPermissionUseCase.java
│   │   │   └── GetUserPermissionUseCase.java
│   │   └── out/
│   │       └── UserExistencePort.java       ← "¿Existe este userId?"
│   └── usecase/
│       ├── CreateRoleUseCaseImpl.java
│       ├── GetRoleUseCaseImpl.java
│       ├── UpdateRoleUseCaseImpl.java
│       ├── DeleteRoleUseCaseImpl.java
│       ├── CreatePermissionUseCaseImpl.java
│       ├── GetPermissionUseCaseImpl.java
│       ├── UpdatePermissionUseCaseImpl.java
│       ├── DeletePermissionUseCaseImpl.java
│       ├── AssignRolePermissionUseCaseImpl.java
│       ├── RevokeRolePermissionUseCaseImpl.java
│       ├── GetRolePermissionUseCaseImpl.java
│       ├── AssignUserPermissionUseCaseImpl.java
│       ├── RevokeUserPermissionUseCaseImpl.java
│       └── GetUserPermissionUseCaseImpl.java
│
├── infrastructure/
│   ├── entity/
│   │   ├── RoleEntity.java
│   │   ├── PermissionEntity.java
│   │   ├── RolePermissionEntity.java
│   │   └── UserPermissionEntity.java
│   ├── repository/
│   │   ├── RoleR2dbcRepository.java
│   │   ├── PermissionR2dbcRepository.java
│   │   ├── RolePermissionR2dbcRepository.java
│   │   └── UserPermissionR2dbcRepository.java
│   ├── mapper/
│   │   ├── RolePersistenceMapper.java
│   │   ├── PermissionPersistenceMapper.java
│   │   ├── RolePermissionPersistenceMapper.java
│   │   └── UserPermissionPersistenceMapper.java
│   └── adapter/
│       ├── RoleRepositoryAdapter.java
│       ├── PermissionRepositoryAdapter.java
│       ├── RolePermissionRepositoryAdapter.java
│       └── UserPermissionRepositoryAdapter.java
│
└── presentation/
    ├── api/
    │   ├── RoleControllerApi.java
    │   ├── PermissionControllerApi.java
    │   ├── RolePermissionControllerApi.java
    │   └── UserPermissionControllerApi.java
    ├── controller/
    │   ├── RoleController.java
    │   ├── PermissionController.java
    │   ├── RolePermissionController.java
    │   └── UserPermissionController.java
    ├── dto/
    │   ├── request/
    │   │   ├── RoleCreateRequest.java
    │   │   ├── RoleUpdateRequest.java
    │   │   ├── PermissionCreateRequest.java
    │   │   ├── PermissionUpdateRequest.java
    │   │   ├── AssignRolePermissionRequest.java
    │   │   └── AssignUserPermissionRequest.java
    │   └── response/
    │       ├── RoleResponse.java
    │       ├── PermissionResponse.java
    │       ├── RolePermissionResponse.java
    │       └── UserPermissionResponse.java
    └── mapper/
        ├── RoleWebMapper.java
        ├── PermissionWebMapper.java
        ├── RolePermissionWebMapper.java
        └── UserPermissionWebMapper.java
```

---

## Integración con módulo `user` (Port Out)

El módulo `access` **nunca importa** nada del módulo `user`. Cuando necesita verificar que un usuario existe:

1. `access` define: `application/ports/out/UserExistencePort.java`
   ```java
   public interface UserExistencePort {
       Mono<Boolean> existsById(UUID userId);
   }
   ```

2. `user` implementa: `infrastructure/adapter/external/UserExistenceAdapter.java`
   ```java
   @Component
   public class UserExistenceAdapter implements UserExistencePort {
       private final UserRepository userRepository;
       // implementa existsById usando userRepository.findById()
   }
   ```

> Mismo patrón que `auth` define `UserIdentityPort` y `user` lo implementa en `UserIdentityAdapter`.

---

## Esquema de BD (según screenshots, todo snake_case)

### Tabla `role`
| Columna | Tipo | PK | Default |
|---------|------|----|---------|
| `id` | uuid | ✅ | `gen_random_uuid()` |
| `name` | varchar(32) | — | — |
| `created_at` | timestamp tz | — | `CURRENT_TIMESTAMP` |
| `updated_at` | timestamp tz | — | — |
| `status_registry` | varchar(20) | — | `'ACTIVE'` |
| `status_updated_at` | timestamp tz | — | — |

### Tabla `permission`
| Columna | Tipo | PK | Default |
|---------|------|----|---------|
| `id` | uuid | ✅ | `gen_random_uuid()` |
| `name` | varchar(100) | — | — |
| `description` | varchar(255) | — | — |
| `created_at` / `updated_at` / `status_registry` / `status_updated_at` | (auditoría) | — | — |

### Tabla `role_permission`
| Columna | Tipo | PK | Default |
|---------|------|----|---------|
| `id` | uuid | ✅ | `gen_random_uuid()` |
| `role_id` | uuid (FK → role) | — | — |
| `permission_id` | uuid (FK → permission) | — | — |
| `created_at` / `updated_at` / `status_registry` / `status_updated_at` | (auditoría) | — | — |

### Tabla `user_permission`
| Columna | Tipo | PK | Default |
|---------|------|----|---------|
| `id` | uuid | ✅ | `gen_random_uuid()` |
| `user_id` | uuid (FK → user) | — | — |
| `permission_id` | uuid (FK → permission) | — | — |
| `is_active` | boolean | — | `true` |
| `created_at` / `updated_at` / `status_registry` / `status_updated_at` | (auditoría) | — | — |

---

## Endpoints REST

### Role CRUD
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/v1/roles` | Listar todos los roles |
| `GET` | `/api/v1/roles/{roleId}` | Obtener rol por ID |
| `POST` | `/api/v1/roles` | Crear rol |
| `PATCH` | `/api/v1/roles/{roleId}` | Actualizar rol |
| `DELETE` | `/api/v1/roles/{roleId}` | Soft delete rol |

### Permission CRUD
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/v1/permissions` | Listar todos los permisos |
| `GET` | `/api/v1/permissions/{permissionId}` | Obtener permiso por ID |
| `POST` | `/api/v1/permissions` | Crear permiso |
| `PATCH` | `/api/v1/permissions/{permissionId}` | Actualizar permiso |
| `DELETE` | `/api/v1/permissions/{permissionId}` | Soft delete permiso |

### Role ↔ Permission
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/v1/roles/{roleId}/permissions` | Permisos de un rol |
| `POST` | `/api/v1/roles/{roleId}/permissions` | Asignar permisos al rol |
| `DELETE` | `/api/v1/roles/{roleId}/permissions/{permissionId}` | Revocar permiso del rol |

### User ↔ Permission
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/v1/users/{userId}/permissions` | Permisos de un usuario |
| `POST` | `/api/v1/users/{userId}/permissions` | Asignar permisos al usuario |
| `DELETE` | `/api/v1/users/{userId}/permissions/{permissionId}` | Revocar permiso del usuario |

---

## Open Questions

> [!IMPORTANT]
> El `PermissionEntity` actual en `permission/infrastructure/` usa PascalCase (`PermissionID`, `PermissionName`, tabla `Permissions`). Las screenshots muestran snake_case. **¿Cuál es el esquema real de la BD?** Asumo snake_case.

> [!WARNING]
> El módulo `permission/` existente (con `PermissionRepositoryCustom`, etc.) **se eliminará** al crear el módulo `access`. ¿Estás de acuerdo?

1. ¿Los endpoints de tablas intermedias aceptan lista de `permissionIds` para asignar varios a la vez?
2. ¿Todos los deletes son soft delete (`status_registry = 'DELETE'`)?
3. ¿Quieres que incluya el `CalculateEffectivePermissionsUseCase` del doc o eso va en otra fase?

---

## Archivos que se tocan fuera de `access`

| Módulo | Archivo | Acción |
|--------|---------|--------|
| `user` | `infrastructure/adapter/external/UserExistenceAdapter.java` | **[NEW]** — Implementa `UserExistencePort` |
| `permission` | Todo el directorio | **[DELETE]** — Se reemplaza por `access` |

---

## Verification Plan

```bash
./mvnw clean compile
```
- Compilación sin errores
- Tests manuales con archivos `.yml` en `api-tests/Tumbes de Miercoles Api/`
