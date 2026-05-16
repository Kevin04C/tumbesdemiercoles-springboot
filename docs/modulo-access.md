# 🛡️ Módulo de Accesos (Access & Authorization)

**Módulo**: `com.tumbesdemiercoles.api.access`

**Propósito**: Gestión centralizada de Roles, Permisos y Políticas de Autorización (RBAC + Anulaciones por Usuario).

---

## 💡 Filosofía del Diseño

En lugar de crear un módulo por cada tabla de base de datos, utilizamos un único **Contexto Delimitado (`access`)** de alta cohesión. Este módulo encapsula la complejidad de las tablas `role`, `permission`, `role_permission` y `user_permission`, exponiendo únicamente la lógica de negocio procesada al resto del sistema.

**¿Por qué esta estructura?**

- **Evita dependencias circulares**: Las 4 tablas trabajan en conjunto para un solo objetivo: determinar accesos.
- **Encapsulamiento**: El módulo `auth` o `security` no necesita saber cómo están estructuradas las tablas relacionales; simplemente le pedirá al módulo `access` la lista final de permisos de un usuario.

---

## 🏗️ Estructura del Módulo `access`

```plaintext
com.tumbesdemiercoles.api.access/
│
├── domain/
│   ├── model/
│   │   ├── Role.java
│   │   ├── Permission.java
│   │   └── EffectiveAccess.java       ← POJO que contiene la lista final de authorities calculados
│   ├── repository/
│   │   ├── RoleRepository.java
│   │   ├── PermissionRepository.java
│   │   └── UserPermissionRepository.java
│   └── exception/
│       └── AccessDeniedDomainException.java
│
├── application/
│   ├── dto/
│   │   ├── AssignRoleDto.java
│   │   └── UpdateUserOverridesDto.java
│   ├── ports/
│   │   ├── in/
│   │   │   ├── ManageRolesUseCase.java
│   │   │   ├── ManageUserOverridesUseCase.java
│   │   │   └── CalculateEffectivePermissionsUseCase.java ← El motor de cálculo
│   │   └── out/
│   │       └── UserCheckPort.java     ← Para verificar que el usuario existe antes de asignarle cosas
│   └── usecase/
│       └── CalculateEffectivePermissionsUseCaseImpl.java
│
├── infrastructure/
│   ├── entity/
│   │   ├── RoleEntity.java
│   │   ├── PermissionEntity.java
│   │   ├── RolePermissionEntity.java
│   │   └── UserPermissionEntity.java  ← Contiene el campo crucial 'is_active'
│   ├── repository/
│   │   └── (R2DBC Repositories para las 4 tablas)
│   ├── mapper/
│   │   └── AccessPersistenceMapper.java
│   └── adapter/
│       └── AccessRepositoryAdapter.java
│
└── presentation/
    ├── controller/
    │   ├── RoleController.java        ← CRUD de Roles (Solo para SuperAdmins)
    │   ├── PermissionController.java  ← CRUD de Permisos (Solo lectura general)
    │   └── UserAccessController.java  ← Endpoints para que el frontend asigne roles y overrides
    └── dto/
        └── request/UserOverridesRequest.java
```

---

## ⚙️ Lógica de Negocio: Permisos Efectivos (Effective Permissions)

El corazón de este módulo es el caso de uso `CalculateEffectivePermissionsUseCase`.

> El sistema opera bajo el modelo **RBAC (Role-Based Access Control)** con excepciones a nivel de usuario.

### El Algoritmo de Resolución

Cuando un usuario hace Login, el sistema de seguridad consulta este módulo para generar el Token JWT con los permisos correctos. El cálculo interno es:

1.  **Obtener Base**: Se extraen todos los permisos asociados al **Rol** del usuario (vía `role_permission`).
2.  **Aplicar Revocaciones**: Se consultan los registros en `user_permission` donde `is_active = false`. Estos permisos se **eliminan** de la lista base.
3.  **Aplicar Concesiones Extra**: Se consultan los registros en `user_permission` donde `is_active = true`. Estos permisos se **añaden** a la lista base (si no existían ya).

---

## Ejemplo de Flujo (Frontend)

1.  El administrador entra a la vista de edición del usuario "Juanito" (Rol: `TEACHER`).
2.  El frontend carga la lista de permisos del rol `TEACHER` y los muestra como checkboxes marcados.
3.  El administrador desmarca `DELETE_COURSE` y guarda.
4.  El controlador de presentación recibe la petición y el Caso de Uso registra/actualiza en la tabla `user_permission`:
    ```json
    {
      "user_id": "[uuid_juanito]",
      "permission_id": "[uuid_delete_course]",
      "is_active": false
    }
    ```
5.  La próxima vez que Juanito inicie sesión, el algoritmo de Permisos Efectivos omitirá la acción de borrar cursos.

---

## 🔌 Integración con el módulo `auth`

Para mantener Clean Architecture, el módulo de Autenticación (`auth`) utilizará un **Out-Port** que será implementado por este módulo `access`.

> `auth` llama a `GetPermissionsForTokenPort(userId)`.
>
> El adaptador externo de `access` ejecuta `CalculateEffectivePermissionsUseCase`.
>
> Retorna un simple `List<String>` (ej. `["CREATE_USER", "READ_COURSE"]`).
>
> `auth` inyecta esa lista directamente en el payload del JWT.
