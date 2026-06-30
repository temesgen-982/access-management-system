# Spring Boot Security Learning Project

Access Management System built with Spring Boot 4.0.5, JPA, Spring Security, and JWT.

## Tech Stack

Java 21, Spring Boot 4.0.5, Maven, H2, Spring Data JPA, Spring Security, JWT (jjwt 0.12.5), Springdoc OpenAPI 2.8.5

## Project Structure

```
springboot/
├── bruno/                   # API client collections
├── docs/                    # Project docs & roadmap
├── src/
│   ├── main/java/com/example/demo/
│   │   ├── config/SecurityConfig.java
│   │   ├── controller/AuthController.java
│   │   ├── controller/UserController.java
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── initializer/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── security/
│   │   └── service/
│   ├── main/resources/application.properties
│   └── test/
├── pom.xml
└── README.md
```

## Run

```bash
./mvnw spring-boot:run
```

## Test

```bash
./mvnw test
```

## Endpoints

| Method | Path | Auth |
|--------|------|------|
| GET | `/status`, `/health`, `/version` | Public |
| POST | `/auth/login` | Public |
| POST | `/users` | Public (register) |
| GET | `/users`, `/users/{id}` | JWT |
| DELETE | `/users/{id}` | ADMIN |
