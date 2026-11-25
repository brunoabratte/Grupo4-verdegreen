# VerdeGreen

Backend REST de VerdeGreen desarrollado con Spring Boot, JPA y MySQL.

- **Group / Artifact:** `com.aydsii` / `demo`
- **Versión:** `0.0.1-SNAPSHOT`
- **Paquete base:** `com.aydsii.demo`

## Requerimientos

| Herramienta | Versión | Notas |
|---|---|---|
| JDK | 25 | Definido en `java.version` del `pom.xml` |
| Maven | 3.9+ | No hace falta instalarlo: se usa el wrapper (`mvnw` / `mvnw.cmd`) |
| MySQL | 8.x | Puede utilizarse XAMPP/Laragon o Docker/Podman |
| Docker o Podman | Opcional | Para levantar MySQL con `compose.yaml` |
| Git | Cualquiera reciente | Para clonar el repositorio |

## Dependencias incluidas

| Dependencia | Versión | Uso |
|---|---|---|
| Spring Boot (parent) | 4.1.1 | Gestiona las versiones de los starters |
| `spring-boot-starter-data-jpa` | 4.1.1 | JPA, Hibernate y HikariCP |
| `spring-boot-starter-webmvc` | 4.1.1 | API REST |
| `spring-boot-starter-validation` | 4.1.1 | Validación de datos (`@Valid`, `@NotNull`, etc.) |
| `spring-boot-starter-log4j2` | 4.1.1 | Logging con Log4j2 |
| `springdoc-openapi-starter-webmvc-ui` | 3.1.0 | Documentación OpenAPI y Swagger UI |
| `mysql-connector-j` | 9.7.0 | Driver de MySQL (runtime) |
| `lombok` | 1.18.46 | Reduce código repetitivo (opcional) |
| `spring-boot-devtools` | 4.1.1 | Recarga automática en desarrollo (runtime, opcional) |
| `spring-boot-starter-data-jpa-test` | 4.1.1 | Tests (scope test) |
| `spring-boot-starter-webmvc-test` | 4.1.1 | Tests (scope test) |

> Logback está excluido de los starters porque se usa Log4j2. Si agregás otro starter de Spring Boot, excluí también `spring-boot-starter-logging`.

## Estructura

```
.
├── compose.yaml
├── pom.xml
└── src
    ├── main
    │   ├── java/com/aydsii/demo
    │   └── resources
    │       ├── application.properties
    │       └── log4j2-spring.xml
    └── test
```

## Puesta en marcha

La aplicación utiliza una base de datos llamada `verdegreen`. Se puede trabajar con una instalación local de MySQL (XAMPP/Laragon) o con MySQL en Docker/Podman.

### Opción A: XAMPP o Laragon

1. Iniciá MySQL desde el panel de XAMPP/Laragon.
2. Verificá desde phpMyAdmin que exista la base de datos `verdegreen`.
3. La configuración predeterminada de `application.properties` utiliza:
   - URL: `jdbc:mysql://localhost:3306/verdegreen`
   - Usuario: `root`
   - Contraseña: vacía

No hace falta definir variables de entorno si tu instalación local utiliza esos valores. Si tu usuario o contraseña son diferentes, definí `DB_USERNAME` y `DB_PASSWORD` antes de ejecutar la aplicación.

### Opción B: MySQL con Docker o Podman

1. Levantá el contenedor:

```bash
# Docker
docker compose up -d

# Podman
podman compose up -d
```

Los valores predeterminados son:

| Variable | Valor predeterminado |
|---|---|
| `MYSQL_DATABASE` | `verdegreen` |
| `MYSQL_USER` | `verdegreen_app` |
| `MYSQL_PASSWORD` | `verdegreen_dev` |
| `MYSQL_ROOT_PASSWORD` | `root` |
| `MYSQL_PORT` | `3306` |

Son credenciales de ejemplo para desarrollo local; no deben reutilizarse en producción.

2. Ejecutá la aplicación indicando el usuario creado por el contenedor.

En **Windows PowerShell**:

```powershell
$env:DB_USERNAME = "verdegreen_app"
$env:DB_PASSWORD = "verdegreen_dev"
.\mvnw.cmd spring-boot:run
```

En **Linux/macOS**:

```bash
DB_USERNAME=verdegreen_app DB_PASSWORD=verdegreen_dev ./mvnw spring-boot:run
```

La URL predeterminada de la aplicación apunta a `localhost:3306`, que es el puerto publicado por Compose.

> **Conflicto de puertos:** si XAMPP ya está utilizando el puerto 3306, detené MySQL de XAMPP antes de levantar el contenedor. Como alternativa, podés publicar Docker en otro puerto, por ejemplo configurando `MYSQL_PORT=3307`, y definir `DB_URL=jdbc:mysql://localhost:3307/verdegreen` al ejecutar la aplicación.

### Ejecutar la aplicación

Si usás XAMPP/Laragon:

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
.\mvnw.cmd spring-boot:run
```

Si usás Docker, seguí los comandos de la opción B para establecer las variables de conexión antes de ejecutar.

### Tests

```bash
# Linux/macOS
./mvnw test

# Windows
.\mvnw.cmd test
```

### Swagger UI

Con la aplicación corriendo: http://localhost:8080/swagger-ui.html

## Logging

La configuración está en `src/main/resources/log4j2-spring.xml`:

- Consola y archivo rotativo en `logs/demo.log`.
- Rotación por día o al alcanzar 10 MB.
- Conserva los archivos rotados durante 30 días.
- Nivel `debug` para `com.aydsii` e `info` para el resto.

## Fork: mismo punto de partida para todos

1. **Hacer el fork:** desde la página del repositorio original, botón **Fork** y elegir tu cuenta.
2. **Clonar tu fork:**

```bash
git clone https://github.com/<tu-usuario>/<repositorio>.git
cd <repositorio>
```

3. **Configurar MySQL:** seguir la opción A o B de la sección anterior.

Cada fork es un proyecto independiente: no se sincroniza automáticamente con el original ni con los forks de los demás.
