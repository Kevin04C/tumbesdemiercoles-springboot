# 🏗️ Clean Architecture — Tumbes de Miércoles API

> Documentación de la arquitectura del proyecto. Sirve como guía y skill para mantener la consistencia al agregar nuevos módulos.

---

## Filosofía

Este proyecto sigue **Clean Architecture (Uncle Bob)** organizada por **módulos de negocio**. Cada módulo contiene sus propias capas internas, lo que permite:

- **Independencia**: cada módulo puede evolucionar sin romper otros.
- **Testabilidad**: el dominio no depende de frameworks.
- **Escalabilidad**: agregar un nuevo módulo es copiar la estructura y llenarla.

---

## Regla de Dependencia

```
Presentation → Application → Domain ← Infrastructure
```

- **Domain** no importa nada de las otras capas (POJO puros).
- **Application** depende solo de Domain.
- **Infrastructure** implementa las interfaces de Domain.
- **Presentation** consume Application y expone HTTP.

---

## Estructura de Carpetas

```
com.tumbesdemiercoles.api/
│
├── 📦 user/                          ← Módulo de negocio
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
│
├── 📦 auth/                          ← Módulo de autenticación
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
│
├── 📦 category/                      ← Módulo de categorías
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── presentation/
│
├── 📦 permission/                     ← Módulo de permisos
│   └── infrastructure/
│
├── 🔒 security/                       ← Cross-cutting (seguridad)
│   ├── configuration/
│   ├── model/
│   ├── presentation/
│   ├── services/
│   └── utils/
│
├── 🟢 shared/                         ← Código compartido entre módulos
│   ├── application/
│   ├── domain/
│   ├── exception/
│   ├── infrastructure/
│   ├── presentation/
│   ├── response/
│   └── utils/
│
└── NewsApiApplication.java
```

---

## Descripción de Capas

### 🟡 Domain — El corazón del negocio

| Carpeta | Contiene | Regla |
|---------|----------|-------|
| `model/` | POJOs puros (User, Product, etc.) | **CERO** anotaciones de Spring/R2DBC/JPA |
| `exception/` | Excepciones de negocio | Extienden `RuntimeException` |
| `repository/` | Interfaces de persistencia | Definen QUÉ, no CÓMO |
| `service/` | Interfaces de operaciones | Contrato para los use cases |

### 🟠 Application — Casos de Uso

| Carpeta | Contiene | Regla |
|---------|----------|-------|
| `dto/` | DTOs de entrada/salida | Transportan datos entre capas |
| `usecase/` | Un `@Service` por operación | `CreateXxxUseCase`, `GetXxxUseCase`, etc. |

### 🔵 Infrastructure — Frameworks y BD

| Carpeta | Contiene | Regla |
|---------|----------|-------|
| `entity/` | Entidades con `@Table`, `@Column` | Solo viven aquí las anotaciones de R2DBC |
| `repository/` | `ReactiveCrudRepository` | Interfaces de Spring Data |
| `mapper/` | Conversores Domain ↔ Entity | Componentes `@Component` |
| `adapter/` | Implementación del repository de domain | Usa el R2DBC repo + mapper |

### 🟣 Presentation — API HTTP

| Carpeta | Contiene | Regla |
|---------|----------|-------|
| `controller/` | `@RestController` | Consume UseCases, no toca Domain directamente |

---

## Cómo Agregar un Nuevo Módulo

Ejemplo: agregar el módulo **`course`**

### 1. Crear la estructura

```
course/
├── domain/
│   ├── model/Course.java
│   ├── exception/CourseNotFoundException.java
│   ├── repository/CourseRepository.java
│   └── service/CourseService.java
├── application/
│   ├── dto/CourseRequestDto.java, CourseResponseDto.java
│   └── usecase/
│       ├── CreateCourseUseCase.java
│       ├── GetCourseUseCase.java
│       ├── UpdateCourseUseCase.java
│       └── DeleteCourseUseCase.java
├── infrastructure/
│   ├── entity/CourseEntity.java
│   ├── repository/CourseR2dbcRepository.java
│   ├── mapper/CoursePersistenceMapper.java
│   └── adapter/CourseRepositoryAdapter.java
└── presentation/
    └── controller/CourseController.java
```

### 2. Package base

```
com.tumbesdemiercoles.api.course.domain.model
com.tumbesdemiercoles.api.course.application.usecase
com.tumbesdemiercoles.api.course.infrastructure.entity
com.tumbesdemiercoles.api.course.presentation.controller
```

### 3. Reglas al implementar

| Capa | Puede importar | NO puede importar |
|------|----------------|-------------------|
| `domain` | Solo Java puro + `reactor` | Spring, R2DBC, otra capa |
| `application` | `domain`, `shared` | `infrastructure`, `presentation` |
| `infrastructure` | `domain`, `shared`, Spring | `application`, `presentation` |
| `presentation` | `application`, `shared` | `domain`, `infrastructure` |

---

## Convenciones de Naming

| Concepto | Patrón | Ejemplo |
|----------|--------|---------|
| Modelo de dominio | `Xxx` | `User`, `Course` |
| Entidad de BD | `XxxEntity` | `UserEntity`, `CourseEntity` |
| DTO de entrada | `XxxRequestDto` | `UserRequestDto` |
| DTO de salida | `XxxResponseDto` | `UserResponseDto` |
| Caso de uso | `VerbXxxUseCase` | `CreateUserUseCase` |
| Repo de dominio | `XxxRepository` | `UserRepository` (interfaz) |
| Repo de Spring | `XxxR2dbcRepository` | `UserR2dbcRepository` |
| Adaptador | `XxxRepositoryAdapter` | `UserRepositoryAdapter` |
| Mapper | `XxxPersistenceMapper` | `UserPersistenceMapper` |
| Controller | `XxxController` | `UserController` |
| Excepción | `XxxNotFoundException` | `UserNotFoundException` |

---

## Stack Tecnológico

- **Java 21** + **Spring Boot 3**
- **Spring WebFlux** (reactivo)
- **Spring Data R2DBC** (persistencia reactiva)
- **Lombok** (@Data, @Builder, @RequiredArgsConstructor)
- **Reactor** (Mono, Flux)
- **JWT** (autenticación stateless)

---

## Notas Importantes

> [!IMPORTANT]
> El modelo de **domain** NUNCA debe tener anotaciones de `@Table`, `@Column`, `@Id` ni ninguna otra de Spring Data. Eso va en `infrastructure/entity/`.

> [!TIP]
> Cada **UseCase** debe ser una clase independiente con un método `execute()` (o nombre descriptivo como `getById()`). No crear servicios monolíticos con 10 métodos.

> [!WARNING]
> Si necesitas compartir un DTO entre módulos, colócalo en `shared/dto/`. No hagas dependencias circulares entre módulos.
