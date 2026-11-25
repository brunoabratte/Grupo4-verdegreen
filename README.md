# VerdeGreen

Backend REST de VerdeGreen desarrollado con Spring Boot, Java, JPA y MySQL.

- **Group / Artifact:** `com.aydsii` / `demo`
- **Versión:** `0.0.1-SNAPSHOT`
- **Paquete base:** `com.aydsii.demo`

## Requerimientos

| Herramienta | Versión | Uso |
|---|---|---|
| JDK | 25 | Ejecución del proyecto |
| Maven | 3.9+ | Gestión y construcción del proyecto |
| MySQL | 8.x | Base de datos |
| Git | Cualquiera reciente | Control de versiones |
| Docker o Podman | Opcional | Ejecución de MySQL en contenedor |

## Tecnologías utilizadas

- Spring Boot y Spring MVC para los servicios REST.
- Spring Data JPA e Hibernate para persistencia.
- MySQL como sistema de gestión de base de datos.
- Springdoc OpenAPI para documentación y Swagger UI.
- SLF4J y Log4j2 para registro de eventos.
- Lombok para reducir código repetitivo.

## Estructura del proyecto

```text
src/
├── main/
│   ├── java/com/aydsii/demo/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── response/
│   │   └── service/
│   └── resources/
│       ├── application.properties
│       └── log4j2-spring.xml
└── test/
```

## Configuración de MySQL

La aplicación utiliza una base de datos llamada `verdegreen`. Se puede trabajar con MySQL instalado localmente mediante XAMPP/Laragon o utilizando Docker/Podman.

### Opción A: XAMPP o Laragon

1. Iniciar MySQL desde el panel de XAMPP o Laragon.
2. Verificar desde phpMyAdmin que exista la base de datos `verdegreen`.
3. La configuración predeterminada utiliza:

   - URL: `jdbc:mysql://localhost:3306/verdegreen`
   - Usuario: `root`
   - Contraseña: vacía

Si la instalación utiliza otros datos de conexión, se pueden configurar mediante las variables de entorno `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`.

### Opción B: Docker o Podman

Levantar el contenedor desde la raíz del proyecto:

```bash
docker compose up -d
```

O utilizando Podman:

```bash
podman compose up -d
```

Valores predeterminados:

| Variable | Valor |
|---|---|
| `MYSQL_DATABASE` | `verdegreen` |
| `MYSQL_USER` | `verdegreen_app` |
| `MYSQL_PASSWORD` | `verdegreen_dev` |
| `MYSQL_ROOT_PASSWORD` | `root` |
| `MYSQL_PORT` | `3306` |

Estas credenciales son únicamente para desarrollo local y deben reemplazarse en cualquier entorno de producción.

Para ejecutar la aplicación con Docker, indicar las credenciales del usuario creado por el contenedor.

En Windows PowerShell:

```powershell
$env:DB_USERNAME = "verdegreen_app"
$env:DB_PASSWORD = "verdegreen_dev"
.\mvnw.cmd spring-boot:run
```

En Linux/macOS:

```bash
DB_USERNAME=verdegreen_app DB_PASSWORD=verdegreen_dev ./mvnw spring-boot:run
```

**Importante:** si XAMPP está utilizando el puerto 3306, detener su servicio MySQL antes de levantar el contenedor. También se puede configurar otro puerto mediante `MYSQL_PORT` y ajustar `DB_URL` para que la aplicación se conecte al puerto correspondiente.

## Ejecución de la aplicación

Con XAMPP/Laragon iniciado y la base de datos configurada:

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

## Servicios REST implementados

| Caso de uso | Endpoint | Método |
|---|---|---|
| CU-02 Consultar catálogo | `/api/productos` | GET |
| CU-04 Consultar historial de ventas | `/api/ventas` | GET |

El historial de ventas admite los parámetros opcionales `desde` y `hasta` para consultar un período determinado.

## Swagger UI

Con la aplicación ejecutándose, acceder a:

`http://localhost:8080/swagger-ui.html`

Desde allí se pueden consultar y probar los servicios REST.

## Logging

La configuración de logging se encuentra en `src/main/resources/log4j2-spring.xml`.

Se utiliza SLF4J como fachada y Log4j2 como implementación. Los registros se muestran en consola y se almacenan en archivos dentro de la carpeta `logs/`.

## Pruebas

Para ejecutar los tests:

Windows:

```powershell
.\mvnw.cmd test
```

Linux/macOS:

```bash
./mvnw test
```
