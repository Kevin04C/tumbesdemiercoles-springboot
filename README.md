# 📰 Tumbes de Miércoles API

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-17-blue?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/MeiliSearch-v1.8-red?style=for-the-badge&logo=meilisearch&logoColor=white" alt="MeiliSearch" />
  <img src="https://img.shields.io/badge/Docker-Enabled-blue?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/Clean%20Architecture-Pattern-lightgrey?style=for-the-badge" alt="Clean Architecture" />
</p>

---

## 💡 Sobre el Proyecto

**Tumbes de Miércoles API** es el motor backend para el portal de noticias y semanario digital del mismo nombre. Este proyecto ha sido desarrollado siguiendo un enfoque de **Clean Architecture** y bajo el paradigma **reactivo** no bloqueante, ofreciendo una alta concurrencia y un excelente rendimiento bajo cargas de trabajo pesadas.

Está diseñado para gestionar artículos periodísticos, columnistas invitados, categorías dinámicas, subida de semanarios digitales en formato PDF (con soporte premium y gratuito), auditoría detallada de base de datos a través de triggers y un potente motor de control de accesos **RBAC con Excepciones a nivel de usuario**.

---

## ✨ Características Principales

*   **⚡ Arquitectura Reactiva**: Implementado completamente con **Spring WebFlux** y **R2DBC** sobre Project Reactor, garantizando operaciones I/O no bloqueantes desde la API hasta la base de datos PostgreSQL.
*   **🛡️ Control de Accesos (RBAC con Excepciones)**: Sistema híbrido de permisos. Los usuarios heredan permisos predeterminados de sus **Roles**, pero el sistema permite definir **anulaciones (overrides)** específicas por usuario (concesiones adicionales o revocaciones selectivas) que se combinan reactivamente en tiempo de ejecución.
*   **🔍 Búsqueda Ultra-Rápida con MeiliSearch**: Sincronización e indexación de noticias con **MeiliSearch** para búsquedas instantáneas con tolerancia a fallas ortográficas y resaltado de coincidencias.
*   **📝 Plataforma de Columnistas**: Módulo dedicado para que columnistas invitados redacten artículos de opinión independientes con su propio perfil y archivo histórico.
*   **📅 Semanario Digital (Digital Weekly)**: Gestión de ediciones en formato PDF, clasificando ediciones normales y ediciones premium (acceso restringido mediante autorización).
*   **🔄 Auditoría de Datos Integrada**: Trigger de PostgreSQL (`fn_audit_logger`) que registra automáticamente en una tabla de auditoría cada creación, modificación o eliminación física realizada sobre registros clave.
*   **✉️ Seguridad & Autenticación**: Autenticación stateless mediante **JWT**, registro seguro de usuarios con verificación por correo SMTP (Thymeleaf templates) y recuperación de contraseña segura utilizando *pepper* dinámico.

---

## 🏗️ Clean Architecture

El proyecto está estructurado siguiendo los principios de la **Arquitectura Limpia (Clean Architecture)** de Robert C. Martin, organizada además en **contextos/módulos de negocio acoplados**.

Cada módulo contiene internamente las siguientes cuatro capas:

```
com.tumbesdemiercoles.api.[modulo]/
├── domain/                  # Lógica pura de negocio (Modelos POJO, Repositorios e interfaces de entrada/salida)
├── application/             # Casos de uso (Lógica que orquesta la aplicación, DTOs de negocio y puertos de entrada/salida)
├── infrastructure/          # Detalles técnicos de Spring Boot, entidades R2DBC (@Table), adaptadores y MapStruct mappers
└── presentation/            # Controladores REST (@RestController) que exponen la API HTTP reactiva
```

Las dependencias fluyen estrictamente hacia adentro:
$$\text{Presentation} \longrightarrow \text{Application} \longrightarrow \text{Domain} \longleftarrow \text{Infrastructure}$$

---

## 🛠️ Stack Tecnológico

*   **Lenguaje**: Java 25
*   **Framework Principal**: Spring Boot 4.0.6 (con Spring WebFlux)
*   **Persistencia Reactiva**: Spring Data R2DBC
*   **Base de Datos**: PostgreSQL 17 (con soporte de auditoría por triggers)
*   **Motor de Búsqueda**: MeiliSearch v1.8 (Java SDK integrado)
*   **Autenticación**: Spring Security + OAuth2 Resource Server & JJWT (Java JWT)
*   **Generador de Mappers**: MapStruct 1.5.5.Final
*   **Manejo de Variables de Entorno**: Spring-Dotenv (carga dinámica desde `.env`)
*   **Pruebas de API**: Colecciones de Bruno

---

## 🚀 Guía de Inicio Rápido

Sigue estos sencillos pasos para clonar, configurar e iniciar el proyecto en tu entorno local.

### 📋 Prerrequisitos

Asegúrate de tener instalado en tu sistema:
1.  **Java 25** (Se recomienda usar [SDKMAN!](https://sdkman.io/) para instalarlo fácilmente).
2.  **Docker & Docker Compose** (Para levantar MeiliSearch y opcionalmente PostgreSQL).
3.  **Git** para el control de versiones.

---

### Paso 1: Clonar el Repositorio

Abre tu terminal y ejecuta el siguiente comando para clonar el proyecto:

```bash
git clone https://github.com/Kevin04C/tumbesdemiercoles-springboot.git
cd tumbesdemiercoles-springboot
```

---

### Paso 2: Configurar las Variables de Entorno (`.env`)

El proyecto utiliza variables de entorno cargadas dinámicamente desde un archivo `.env` en la raíz. Copia la plantilla de ejemplo y configúrala:

```bash
cp .env.example .env
```

Abre el archivo `.env` recién creado y define los valores necesarios. A continuación se detallan las variables obligatorias para el arranque:

| Variable | Propósito | Valor de Ejemplo / Sugerido |
| :--- | :--- | :--- |
| `DB_URL` | URL de conexión R2DBC para PostgreSQL | `r2dbc:postgresql://localhost:5432/tumbes` |
| `DB_USERNAME` | Usuario de la base de datos | `postgres` |
| `DB_PASSWORD` | Contraseña de la base de datos | `postgres` |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT | *Usa una cadena larga de al menos 256 bits* |
| `SMTP_USERNAME` | Correo SMTP de origen para notificaciones | `tu-correo@gmail.com` |
| `SMTP_PASSWORD` | Contraseña de aplicación de tu correo | `abcd-efgh-ijkl-mnop` |
| `VERIFY_URL` | URL del frontend para verificar cuenta | `http://localhost:3000/verify` |
| `RESET_URL` | URL del frontend para recuperar clave | `http://localhost:3000/reset-password` |
| `SECURITY_PEPPER` | Semilla extra para encriptación | *Una cadena aleatoria* |
| `MEILI_HOST` | Host del buscador MeiliSearch | `http://localhost:7700` |
| `MEILI_API_KEY` | Clave Maestra de MeiliSearch | `tumbes_master_key_123` |
| `MEILI_MASTER_KEY` | Clave Maestra de MeiliSearch (interno) | `tumbes_master_key_123` |
| `ADMIN_API_KEY` | Llave API privada para tareas administrativas | `admin_super_secret_key` |

---

### Paso 3: Inicializar la Base de Datos (PostgreSQL)

> [!NOTE]
> Este proyecto está configurado por defecto para apuntar a una base de datos PostgreSQL **externa o local**. No se levanta una instancia de PostgreSQL automáticamente dentro del archivo `docker-compose.yml` para brindar flexibilidad al desarrollador.

#### Opción A: Levantar PostgreSQL en Docker (Recomendado si no tienes local)
Si no tienes un servidor PostgreSQL corriendo en tu máquina, levanta un contenedor rápidamente con este comando:

```bash
docker run --name postgres-tumbes -e POSTGRES_DB=tumbes -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:17
```

#### Opción B: Restaurar el Dump y la Semilla de Datos
El proyecto incluye un script SQL listo con toda la estructura y datos de prueba. Importa el archivo utilizando tu terminal o cliente SQL favorito (DBeaver, pgAdmin, DataGrip):

*   **Usando la terminal (psql)**:
    ```bash
    psql -h localhost -U postgres -d tumbes -f src/main/resources/database/script_tumbes_de_miercoles.sql
    ```

---

### Paso 4: Levantar Servicios Adicionales (MeiliSearch)

Levanta la instancia de **MeiliSearch** local ejecutando el archivo Docker Compose incluido en el proyecto:

```bash
docker compose up -d
```

Esto iniciará MeiliSearch en el puerto `7700`.

---

### Paso 5: Compilar y Ejecutar la Aplicación

Usa el Maven Wrapper incluido en el proyecto para compilar e iniciar el servidor reactivo.

*   **En Linux / macOS**:
    ```bash
    ./mvnw spring-boot:run
    ```

*   **En Windows**:
    ```powershell
    .\mvnw.cmd spring-boot:run
    ```

La aplicación arrancará y por defecto escuchará peticiones HTTP en el puerto **`8080`** (o en el puerto configurado si se corre dentro del contenedor Docker como `8081`).

---

## 🔑 Credenciales de Prueba (Semilla)

Para realizar pruebas rápidas una vez levantada la base de datos del script semilla, puedes usar la siguiente cuenta de administrador/usuario pre-cargada:

*   **Email**: `gavino_10@hotmail.es`
*   **Contraseña**: `franz123`

---

## 🧪 Pruebas de API con Bruno

El proyecto cuenta con una suite completa de pruebas de API almacenada en el directorio [api-tests](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/api-tests). Esta colección está configurada para **Bruno**, un cliente API Git-friendly open-source alternativo a Postman.

### ¿Cómo usarlos?
1.  Descarga e instala [Bruno](https://www.usebruno.com/).
2.  Abre Bruno y selecciona **"Open Collection"**.
3.  Selecciona la carpeta [api-tests/Tumbes de Miercoles Api](file:///c:/Users/franz%20schwartz/Documents/PROYECTOS-PERSONALES/tumbesdemiercoles-springboot/api-tests/Tumbes%20de%20Miercoles%20Api) de este repositorio.
4.  Carga el entorno de desarrollo (`environments/DEVELOPMENT.bru` u homólogo) y ejecuta las peticiones REST organizadas por carpetas (`AUTH`, `ACCESS`, `USER`, `CATEGORY`).

---

## 🛠️ Comandos Útiles de Desarrollo

*   **Generar Mappers de MapStruct**:
    ```bash
    ./mvnw clean test-compile
    ```
*   **Compilar y Empaquetar omitiendo pruebas unitarias**:
    ```bash
    ./mvnw clean package -DskipTests
    ```
*   **Correr Tests Unitarios y de Integración**:
    ```bash
    ./mvnw clean test
    ```
*   **Verificación del Estilo de Código (Checkstyle)**:
    El proyecto cuenta con un archivo `chekstyle.xml` estricto para asegurar la consistencia estilística. Puedes verificarlo con tu plugin IDE favorito o Maven.

---

## 📄 Licencia

Este proyecto es privado. Todos los derechos reservados a **Tumbes de Miércoles**.
