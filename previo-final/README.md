# Examen Final — Programación Web UFPS

Proyecto Spring Boot que implementa los 5 módulos del examen final.

## Requisitos

- Java 17+
- Maven 3.6+ (o usar `./mvnw` incluido)
- IntelliJ IDEA (Community o Ultimate)

## Cómo ejecutar en IntelliJ

1. **Abrir el proyecto:** `File → Open` → selecciona la carpeta `previo-final`
2. IntelliJ detectará automáticamente el proyecto Maven
3. Esperar a que descargue dependencias (primer inicio puede tardar)
4. Ejecutar `PrevioFinalApplication.java` (botón ▶ verde)
5. Abrir en el navegador: `http://localhost:8080`

## Usuarios de prueba

| Usuario | Contraseña | Rol   |
|---------|-----------|-------|
| admin   | admin123  | ADMIN |
| user1   | user123   | USER  |
| user2   | user123   | USER  |

## Endpoints disponibles

### Módulo 1 — Mensajes Internos (todos requieren autenticación)

```
POST   /api/mensajes                    → Enviar mensaje (body JSON)
GET    /api/mensajes/bandeja-entrada    → Mis mensajes recibidos
GET    /api/mensajes/enviados           → Mis mensajes enviados
PUT    /api/mensajes/{id}/leer          → Marcar como leído
GET    /api/mensajes/no-leidos/count    → {"count": N}
```

**Body para POST /api/mensajes:**
```json
{
  "usernameReceptor": "user2",
  "asunto": "Hola",
  "contenido": "Este es el mensaje"
}
```

### Módulo 2 — Solicitudes (flujo de estados)

```
POST   /api/solicitudes                     → Radicar solicitud (autenticado) → 201
GET    /api/solicitudes/mis-solicitudes     → Mis solicitudes (autenticado)
GET    /api/solicitudes                     → Todas (solo ADMIN)
PUT    /api/solicitudes/{id}/aprobar?observacion=texto  → ADMIN → 200
PUT    /api/solicitudes/{id}/rechazar?observacion=texto → ADMIN → 200
```

**Body para POST /api/solicitudes:**
```json
{
  "tipo": "SOPORTE",
  "descripcion": "Necesito ayuda con el sistema"
}
```
Tipos válidos: `SOPORTE`, `ACCESO`, `INFORMACION`

### Módulo 3 — Panel Visual (solo ADMIN)

```
GET /admin/solicitudes/panel
```

Acceder desde el navegador con sesión de `admin`.

### H2 Console (debug)

```
GET /h2-console
JDBC URL: jdbc:h2:mem:testdb
User: sa  |  Password: (vacío)
```

## Ejecutar pruebas

```bash
# Todas las pruebas
./mvnw test

# Solo Módulo 4
./mvnw test -Dtest=MensajeControllerTest

# Solo Módulo 5
./mvnw test -Dtest=SolicitudSecurityTest
```

## Estructura del proyecto

```
src/
├── main/java/com/ufps/previofinal/
│   ├── PrevioFinalApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── DataInitializer.java
│   │   ├── HomeController.java
│   │   └── GlobalExceptionHandler.java
│   ├── usuario/
│   │   ├── Usuario.java
│   │   ├── UsuarioRepository.java
│   │   └── CustomUserDetailsService.java
│   ├── mensaje/
│   │   ├── Mensaje.java
│   │   ├── MensajeDTO.java
│   │   ├── MensajeRepository.java
│   │   ├── MensajeService.java
│   │   └── MensajeController.java
│   └── solicitud/
│       ├── TipoSolicitud.java (enum)
│       ├── EstadoSolicitud.java (enum)
│       ├── Solicitud.java
│       ├── SolicitudDTO.java
│       ├── SolicitudRepository.java
│       ├── SolicitudService.java
│       ├── SolicitudController.java
│       └── PanelAdminController.java
├── main/resources/
│   ├── application.properties
│   └── templates/
│       ├── login.html
│       ├── home.html
│       ├── 403.html
│       └── admin/panel-solicitudes.html
└── test/java/com/ufps/previofinal/
    ├── PrevioFinalApplicationTests.java
    ├── mensaje/MensajeControllerTest.java   (Módulo 4)
    └── solicitud/SolicitudSecurityTest.java (Módulo 5)
```

## Notas de implementación

- Base de datos H2 en memoria (no requiere MySQL ni configuración externa)
- Spring Security con BCrypt para contraseñas
- `@PreAuthorize("hasRole('ADMIN')")` en endpoints de administrador
- Validación con Bean Validation (`@NotBlank`, `@NotNull`)
- Manejo de errores centralizado con `@RestControllerAdvice`
- CSRF deshabilitado para rutas `/api/**` (para Postman)
