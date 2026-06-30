# Codebase Context

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.5 |
| Build | Maven |
| Database | H2 (runtime, in-memory) |
| ORM | Spring Data JPA / Hibernate |
| Web | Spring MVC |
| Security | Spring Security + JWT (jjwt 0.12.5) |
| Validation | Bean Validation (`spring-boot-starter-validation`) |
| API Client | Bruno (collections in `/bruno`) |
| API Docs | Springdoc OpenAPI 2.8.5 |
| Tooling | Lombok |

## Project Structure

```
springboot/
├── bruno/                   # API client collections
├── docs/
│   ├── roadmap.md
│   ├── codebaseStatus.md
│   └── codebaseContext.md
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java
│   │   │   ├── HelloController.java
│   │   │   ├── config/
│   │   │   │   ├── LabSecurityExpression.java
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuditLogController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/
│   │   │   │   ├── AuditLogResponse.java
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── CreateUserRequest.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   └── UserResponse.java
│   │   │   ├── event/
│   │   │   │   ├── AuditActionEvt.java
│   │   │   │   └── AuditEventListener.java
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── initializer/
│   │   │   │   └── DataInitializer.java
│   │   │   ├── model/
│   │   │   │   ├── AuditLog.java
│   │   │   │   ├── Department.java
│   │   │   │   ├── Permission.java
│   │   │   │   ├── Role.java
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   ├── AuditLogRepository.java
│   │   │   │   ├── DepartmentRepository.java
│   │   │   │   ├── PermissionRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/
│   │   │   │   └── JwtAuthenticationFilter.java
│   │   │   └── service/
│   │   │       ├── AuditLogService.java
│   │   │       ├── CustomUserDetailsService.java
│   │   │       ├── JwtService.java
│   │   │       └── UserService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/demo/
│           └── DemoApplicationTests.java
├── pom.xml
└── ...
```

## Base Package

`com.example.demo`

## Running

```bash
./mvnw spring-boot:run
```

## Testing

```bash
./mvnw test
```

## Current Endpoints

### Public
| Method | Path | Returns |
|--------|------|---------|
| GET | `/status` | `"Hello from the world of Java"` |
| GET | `/health` | `{"status": "UP"}` |
| GET | `/version` | `{"version": "1.0.0-SNAPSHOT"}` |
| POST | `/auth/login` | `{"token": "jwt..."}` |
| POST | `/users` | `UserResponse` (registration) |

### Authenticated (JWT required)
| Method | Path | Returns |
|--------|------|---------|
| GET | `/users` | `List<UserResponse>` |
| GET | `/users/{id}` | `UserResponse` |
| DELETE | `/users/{id}` | `204 No Content` |

### Admin Only (`VIEW_AUDIT_LOGS`)
| Method | Path | Returns |
|--------|------|---------|
| GET | `/admin/audit-logs` | `List<AuditLogResponse>` |

### Swagger UI (public)
| Method | Path | Returns |
|--------|------|---------|
| GET | `/swagger-ui.html` | Swagger UI |
| GET | `/swagger-ui/**` | Swagger assets |
| GET | `/v3/api-docs/**` | OpenAPI JSON spec |

## Security

- JWT-based stateless authentication
- `Authorization: Bearer <token>` header required for protected endpoints
- BCrypt password hashing
- Session management: STATELESS
- `@EnableMethodSecurity` — method-level authorization via `@PreAuthorize`
- Custom `JwtAuthenticationFilter` runs before `UsernamePasswordAuthenticationFilter`
- `User` ↔ `Role` `@ManyToMany` via `user_roles` join table (FetchType.EAGER)
- `Role` ↔ `Permission` `@ManyToMany` via `role_permissions` join table (FetchType.EAGER)
- `User` ↔ `Department` `@ManyToOne` via `department_id` foreign key (FetchType.LAZY)
- `LabSecurityExpression` — custom SpEL bean for data-level department checks
- `GET/DELETE /users/{id}` require matching department (admins exempt)

## Configuration

| Key | Value |
|-----|-------|
| `spring.application.name` | demo |
| Root logging | `WARN` |
| App package logging | `INFO` |
| Spring Web logging | `WARN` |

## Key Conventions

- Constructor injection over `@Autowired`
- DTOs never expose entities (password excluded from responses)
- Entity → Response conversion in controller layer
- Validation via `jakarta.validation` annotations
- Global exception handling via `@ControllerAdvice`
- H2 console enabled
- JWT secret key hardcoded (dev only — move to env/properties for production)
- `@EnableAsync` on `DemoApplication` — audit events processed on background threads
- Event-driven audit pipeline: `UserService` publishes `AuditActionEvt`, `AuditEventListener` persists asynchronously
