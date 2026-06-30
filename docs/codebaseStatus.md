# Codebase Status

## Project
Spring Boot 4.0.5 / Java 21 / Maven

## Implemented

### Application
- [x] `DemoApplication` — main entry point
- [x] `HelloController` — `GET /status`, `GET /health`, `GET /version`

### Phase 1 — Database & JPA
- [x] `User` entity (id, firstName, lastName, email, password, createdAt, updatedAt)
- [x] `UserRepository` (JpaRepository with `findByEmail`)
- [x] `UserService` (CRUD + password hashing)
- [x] `UserController` (`POST/GET /users`, `GET/DELETE /users/{id}`)

### Phase 2 — DTOs & Validation
- [x] `CreateUserRequest` (`@NotBlank`, `@Email`, `@Size`, `@NotNull departmentId`)
- [x] `UserResponse` (excludes password, includes departmentName)
- [x] Bean Validation (`spring-boot-starter-validation`)

### Phase 3 — Exception Handling
- [x] `GlobalExceptionHandler` (`@ControllerAdvice`)
- [x] `ErrorResponse` DTO with field-level errors
- [x] Validation error handling (`MethodArgumentNotValidException`)
- [x] Generic catch-all handler

### Phase 4 — Spring Security
- [x] `spring-boot-starter-security` dependency
- [x] `SecurityConfig` with stateless session policy
- [x] `@EnableMethodSecurity` — method-level authorization
- [x] Public endpoints: `/status`, `/health`, `/version`, `/h2-console/**`, `POST /users`, `/auth/**`
- [x] All other endpoints authenticated

### Phase 5 — Database Authentication
- [x] `CustomUserDetailsService` (loads users from DB by email)
- [x] `User` implements `UserDetails`
- [x] BCrypt password encoding

### Phase 6 — JWT Authentication
- [x] JJWT dependencies (api, impl, jackson)
- [x] `JwtService` (generate, validate, extract claims)
- [x] `JwtAuthenticationFilter` (OncePerRequestFilter)
- [x] `AuthController` (`POST /auth/login`)
- [x] `LoginRequest` / `AuthResponse` DTOs

### Phase 7 — Roles
- [x] `Role` entity (id, name)
- [x] `RoleRepository` (JpaRepository with `findByName`)
- [x] `DataInitializer` (seeds ROLE_USER, ROLE_ADMIN on startup)
- [x] `User` ↔ `Role` `@ManyToMany` relationship (via `user_roles` join table)
- [x] `User.getAuthorities()` dynamically reads from assigned roles
- [x] `UserService.createUser` assigns `ROLE_USER` by default

### Phase 8 — Permissions
- [x] `Permission` entity (id, name)
- [x] `PermissionRepository` (JpaRepository with `findByName`)
- [x] `Role` ↔ `Permission` `@ManyToMany` relationship (via `role_permissions` join table)
- [x] `User.getAuthorities()` grants both role and permission authorities
- [x] `GET /users` — `@PreAuthorize("hasAuthority('VIEW_USERS')")`
- [x] `GET /users/{id}` — `@PreAuthorize("hasAuthority('VIEW_USERS')")`
- [x] `DELETE /users/{id}` — `@PreAuthorize("hasAuthority('DELETE_USER')")`

### Phase 9 — Department System
- [x] `Department` entity (id, name, `@OneToMany` users)
- [x] `DepartmentRepository` (JpaRepository with `findByName`)
- [x] `User` ↔ `Department` `@ManyToOne` relationship
- [x] `User.getDepartmentId()` helper method
- [x] `DataInitializer` seeds departments (Pathology, Hematology, etc.)
- [x] `CreateUserRequest` includes `departmentId` (`@NotNull`)
- [x] `UserService.createUser` assigns department to new user
- [x] `UserResponse` includes `departmentName`

### Phase 10 — Department-Based Access
- [x] `LabSecurityExpression` custom SpEL component
- [x] `canAccessUser(principal, targetUserId)` — data-level check
- [x] Admins bypass department restriction
- [x] Non-admins can only access users in their own department
- [x] `GET /users/{id}` — combined permission + department check
- [x] `DELETE /users/{id}` — combined permission + department check

### Phase 12 — API Documentation
- [x] `OpenApiConfig` — OpenAPI bean with bearer auth scheme
- [x] Swagger UI paths permitted in `SecurityConfig` (`/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`)

### Phase 11 — Audit Logging
- [x] `AuditLog` entity (id, action, performedBy, details, timestamp)
- [x] `AuditLogRepository` (JpaRepository)
- [x] `AuditActionEvt` — custom Spring event class
- [x] `AuditEventListener` — `@Async` + `@EventListener` handler
- [x] `@EnableAsync` on `DemoApplication`
- [x] `UserService` publishes `USER_REGISTRATION` and `USER_DELETION` events
- [x] `AuditLogResponse` DTO
- [x] `AuditLogService` — fetches logs sorted newest-first
- [x] `AuditLogController` — `GET /admin/audit-logs` (secured with `VIEW_AUDIT_LOGS`)
- [x] `DataInitializer` seeds `VIEW_AUDIT_LOGS` permission for `ROLE_ADMIN`

### Other
- [x] Springdoc OpenAPI 2.8.5
- [x] H2 database configured
- [x] Logging configuration (root WARN, app INFO)
- [x] Bruno API client collections for all endpoints

## Roadmap Progress

| Phase | Status |
|-------|--------|
| 0 — Foundation Setup | Completed |
| 1 — Database & JPA | Completed |
| 2 — DTOs & Validation | Completed |
| 3 — Exception Handling | Completed |
| 4 — Spring Security Basics | Completed |
| 5 — Database Authentication | Completed |
| 6 — JWT Authentication | Completed |
| 7 — Roles | Completed |
| 8 — Permissions | Completed |
| 9 — Department System | Completed |
| 10 — Department-Based Access | Completed |
| 11 — Audit Logging | Completed |
| 12 — API Documentation | Completed |
| 13 — PostgreSQL Migration | Not started |
| 14 — Testing | Not started |

## Next Up

- Phase 13 — PostgreSQL migration
- Phase 14 — Testing
