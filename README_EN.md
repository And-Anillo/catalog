# Catalog Management System - User Story Sprint 4 & 5

A production-grade Spring Boot 3.5.7 application implementing comprehensive task management with advanced features including JPA relationships, optimized transactions, error handling, observability, and JWT-based security.

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Technology Stack](#technology-stack)
4. [Project Structure](#project-structure)
5. [Getting Started](#getting-started)
6. [API Authentication](#api-authentication)
7. [API Endpoints](#api-endpoints)
8. [Error Handling](#error-handling)
9. [Logging & Tracing](#logging--tracing)
10. [Security & Authorization](#security--authorization)
11. [Database Schema](#database-schema)
12. [Configuration](#configuration)
13. [Development Guidelines](#development-guidelines)
14. [Testing](#testing)

---

## Overview

The Catalog Management System is a comprehensive Spring Boot application that demonstrates enterprise-level patterns and practices. It covers two major development sprints:

- **Sprint 4 (HU-semana4):** Task Management with JPA Relationships and Optimized Transactions
- **Sprint 5 (HU-semana5):** Error Management & Security Infrastructure

The application provides:
- User management with role-based access control
- Task management with rich domain models and relationships
- Centralized error handling following RFC 7807 standard
- Comprehensive logging with distributed trace correlation
- JWT-based stateless authentication
- Production-ready security configuration

---

## Features

### Sprint 4: Task Management & JPA Relationships

#### User Management
- User creation with validation (email, strong password)
- User profile management
- Role-based user classification (ADMIN, USER)
- User activity tracking through tasks

#### Task Management
- Create, read, update, delete operations for tasks
- Task assignment to users
- Task status management (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
- Task priority levels (LOW, MEDIUM, HIGH, CRITICAL)
- Due date tracking with deadline monitoring
- Task descriptions and detailed comments

#### Relationship Management
- One-to-Many relationship between User and Task
- Cascade operations for dependent records
- Lazy loading optimization strategies
- Entity validation with custom constraints

#### Transaction Optimization
- Declarative transaction management with @Transactional
- Read-only transaction optimization
- Isolation level configuration
- Proper exception handling with rollback semantics

### Sprint 5: Error Management & Security

#### Error Handling (RFC 7807 Compliance)
- Centralized exception handling with @RestControllerAdvice
- Standardized error response format with:
  - HTTP status codes
  - Machine-readable error codes
  - Human-readable error messages
  - Request trace IDs for log correlation
  - Field-level validation errors
  - Timestamps for error tracking

#### Input Validation
- Custom validation annotations:
  - `@ValidEmail` - RFC 5322 compliant email validation
  - `@StrongPassword` - Password strength requirements (8+ chars, uppercase, lowercase, digit, special char)
- Jakarta Bean Validation integration
- Method-level and field-level validation
- Global validation error aggregation

#### Observability & Logging
- Distributed tracing with trace IDs (X-Trace-Id header)
- SLF4J MDC integration for automatic context propagation
- AOP-based method logging with execution timing
- Asynchronous logging for performance
- Rolling file appenders with retention policies
- Color-coded console output for development

#### Security & Authentication
- Spring Security framework integration
- JWT (JSON Web Token) authentication using HS256 algorithm
- Role-based access control (RBAC)
- Bearer token validation
- Password encryption with BCrypt
- Test credentials for development (admin/admin123, user/user123)
- CSRF protection disabled for stateless API
- H2 database console access for development

---

## Technology Stack

### Core Framework
- **Spring Boot 3.5.7** - Application framework
- **Java 17** - Programming language
- **Maven 3.9.x** - Build tool

### Persistence & Database
- **Spring Data JPA** - ORM and data access layer
- **Hibernate** - JPA implementation with lazy loading optimization
- **Flyway** - Database migration and schema versioning
- **H2 Database** - In-memory/file-based relational database
- **MySQL** - Optional production database (configured via properties)

### Security
- **Spring Security** - Authentication and authorization framework
- **JJWT 0.12.3** - JWT token generation and validation (io.jsonwebtoken)
- **BCrypt** - Password encoding

### Logging & Observability
- **SLF4J** - Logging facade
- **Logback** - Logging implementation with async appenders
- **AspectJ** - AOP framework for cross-cutting concerns
- **Spring AOP** - Aspect-oriented programming support

### Validation
- **Jakarta Bean Validation (javax.validation)** - Input validation framework
- **Hibernate Validator** - Validation provider

### Development & Testing
- **Lombok** - Boilerplate code reduction (@Data, @Slf4j, @AllArgsConstructor)
- **Spring Test** - Testing framework
- **JUnit 5** - Test runner

---

## Project Structure

```
catalog/
├── src/
│   ├── main/
│   │   ├── java/com/riwi/catalog/
│   │   │   ├── CatalogApplication.java          # Main application class
│   │   │   ├── application/
│   │   │   │   ├── dto/                         # Data Transfer Objects
│   │   │   │   │   ├── UsuarioCreateDTO.java
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   └── AuthResponse.java
│   │   │   │   ├── entity/                      # Domain entities
│   │   │   │   ├── mapper/                      # Entity/DTO mappers
│   │   │   │   ├── usecase/                     # Business logic (use cases)
│   │   │   │   └── repository/                  # Data access repositories
│   │   │   └── infrastructure/
│   │   │       ├── exception/                   # Exception handling
│   │   │       │   ├── CatalogException.java
│   │   │       │   ├── ResourceNotFoundException.java
│   │   │       │   ├── BusinessConflictException.java
│   │   │       │   ├── ValidationException.java
│   │   │       │   ├── UnauthorizedException.java
│   │   │       │   ├── ForbiddenException.java
│   │   │       │   ├── ErrorResponse.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── controller/                  # REST controllers
│   │   │       │   ├── AuthController.java
│   │   │       │   └── (other endpoint controllers)
│   │   │       ├── logging/                     # Observability infrastructure
│   │   │       │   ├── TraceContext.java
│   │   │       │   ├── TraceIdInterceptor.java
│   │   │       │   ├── LoggingConfiguration.java
│   │   │       │   ├── LoggingAspect.java
│   │   │       │   └── logback-spring.xml
│   │   │       ├── security/                    # Security configuration
│   │   │       │   ├── JwtTokenProvider.java
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   └── SecurityConfiguration.java
│   │   │       └── validation/                  # Custom validators
│   │   │           ├── ValidEmail.java
│   │   │           ├── ValidEmailValidator.java
│   │   │           ├── StrongPassword.java
│   │   │           └── StrongPasswordValidator.java
│   │   └── resources/
│   │       └── application.properties           # Application configuration
│   └── test/
│       └── java/com/riwi/catalog/
│           └── CatalogApplicationTests.java
├── pom.xml                                      # Maven dependencies and build config
├── mvnw & mvnw.cmd                              # Maven wrapper scripts
└── README_EN.md                                 # This file
```

---

## Getting Started

### Prerequisites

- **Java 17 or higher**
- **Maven 3.6.x or higher** (or use included mvnw wrapper)
- **Git**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/And-Anillo/catalog.git
   cd catalog
   ```

2. **Build the project**
   ```bash
   # Using Maven wrapper (recommended)
   ./mvnw clean install
   
   # Or using system Maven
   mvn clean install
   ```

3. **Run the application**
   ```bash
   # Using Maven wrapper
   ./mvnw spring-boot:run
   
   # Or using JAR
   java -jar target/catalog-0.0.1-SNAPSHOT.jar
   ```

4. **Verify the application is running**
   ```bash
   curl http://localhost:8080/api/v1/health
   ```

### Configuration

The application uses `application.properties` for configuration:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/
spring.application.name=catalog

# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=validate

# H2 Console (Development Only)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JWT Configuration
jwt.secret=${JWT_SECRET:your-secret-key-min-256-bits-long-for-hs256-algorithm}
jwt.expirationMs=86400000  # 24 hours in milliseconds

# Logging Configuration
logging.level.com.riwi.catalog=DEBUG
logging.level.org.springframework=INFO
logging.pattern.console=[%thread] %-5level %logger{36} - %msg%n
```

---

## API Authentication

### Overview

The API uses JWT (JSON Web Tokens) with Bearer token authentication. All protected endpoints require a valid JWT token in the `Authorization` header.

### Authentication Flow

1. **Client sends credentials to login endpoint**
   ```http
   POST /api/v1/auth/login HTTP/1.1
   Content-Type: application/json
   
   {
     "username": "admin",
     "password": "admin123"
   }
   ```

2. **Server validates credentials and returns JWT token**
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTcwMzQzMjAwMCwiZXhwIjoxNzAzNTE4NDAwfQ.signature",
     "username": "admin",
     "role": "ADMIN",
     "expiresIn": 86400
   }
   ```

3. **Client includes token in Authorization header for subsequent requests**
   ```http
   GET /api/v1/usuarios HTTP/1.1
   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTcwMzQzMjAwMCwiZXhwIjoxNzAzNTE4NDAwfQ.signature
   ```

### Test Credentials

The application includes pre-configured test credentials:

| Username | Password    | Role  | Permissions                                           |
|----------|-------------|-------|-------------------------------------------------------|
| admin    | admin123    | ADMIN | Create/Update/Delete users and tasks, view all data   |
| user     | user123     | USER  | Create/Update/Delete own tasks, view own data         |

### Token Structure

JWT tokens are signed using **HS256** (HMAC with SHA-256) algorithm:

- **Header:** `{"alg": "HS256", "typ": "JWT"}`
- **Payload:** Contains subject (username), role, issued-at, and expiration claims
- **Signature:** Generated using application secret key (minimum 256 bits for HS256)

### Token Expiration

Default token expiration is **24 hours** (86400 seconds). Configure via `jwt.expirationMs` property.

---

## API Endpoints

### Authentication Endpoints

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "role": "ADMIN",
  "expiresIn": 86400
}
```

**Error Response (401 Unauthorized):**
```json
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Invalid credentials",
  "instance": "/api/v1/auth/login",
  "errorCode": "UNAUTHORIZED",
  "timestamp": "2024-01-01T12:00:00",
  "traceId": "550e8400-e29b-41d4-a716-446655440000"
}
```

### Health Check

#### Check Application Health
```http
GET /api/v1/health
```

**Response (200 OK):**
```json
{
  "status": "UP"
}
```

### User Endpoints

#### Get All Users
```http
GET /api/v1/usuarios
Authorization: Bearer {token}
```

**Required Role:** ADMIN or USER

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "John Doe",
    "email": "john@example.com",
    "rol": "ADMIN",
    "activo": true
  }
]
```

#### Create User
```http
POST /api/v1/usuarios
Authorization: Bearer {token}
Content-Type: application/json

{
  "nombre": "Jane Smith",
  "email": "jane@example.com",
  "password": "StrongPass123!",
  "rol": "USER"
}
```

**Required Role:** ADMIN

**Validation Rules:**
- `nombre` (name): Required, 1-100 characters
- `email`: Required, must be valid email (RFC 5322)
- `password`: Required, minimum 8 characters, must contain:
  - At least one uppercase letter (A-Z)
  - At least one lowercase letter (a-z)
  - At least one digit (0-9)
  - At least one special character (!@#$%^&*)
- `rol` (role): Required, must be ADMIN or USER

**Response (201 Created):**
```json
{
  "id": 2,
  "nombre": "Jane Smith",
  "email": "jane@example.com",
  "rol": "USER",
  "activo": true
}
```

**Error Response (400 Bad Request - Validation Error):**
```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed",
  "instance": "/api/v1/usuarios",
  "errorCode": "VALIDATION_ERROR",
  "timestamp": "2024-01-01T12:00:00",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "errors": {
    "email": ["Email must be valid"],
    "password": ["Password must contain at least one uppercase letter"]
  }
}
```

#### Get User by ID
```http
GET /api/v1/usuarios/{id}
Authorization: Bearer {token}
```

**Required Role:** ADMIN or USER

**Response (200 OK):**
```json
{
  "id": 1,
  "nombre": "John Doe",
  "email": "john@example.com",
  "rol": "ADMIN",
  "activo": true
}
```

**Error Response (404 Not Found):**
```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "User with ID 999 not found",
  "instance": "/api/v1/usuarios/999",
  "errorCode": "RESOURCE_NOT_FOUND",
  "timestamp": "2024-01-01T12:00:00",
  "traceId": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### Update User
```http
PUT /api/v1/usuarios/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nombre": "Jane Doe",
  "email": "jane.doe@example.com",
  "rol": "USER"
}
```

**Required Role:** ADMIN

**Response (200 OK):**
```json
{
  "id": 2,
  "nombre": "Jane Doe",
  "email": "jane.doe@example.com",
  "rol": "USER",
  "activo": true
}
```

#### Delete User
```http
DELETE /api/v1/usuarios/{id}
Authorization: Bearer {token}
```

**Required Role:** ADMIN

**Response (204 No Content)**

### Task Endpoints

#### Get All Tasks (User's Tasks)
```http
GET /api/v1/tareas
Authorization: Bearer {token}
```

**Required Role:** ADMIN or USER

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "titulo": "Complete project documentation",
    "descripcion": "Write comprehensive API documentation",
    "estado": "IN_PROGRESS",
    "prioridad": "HIGH",
    "usuarioId": 1,
    "fechaVencimiento": "2024-01-15",
    "fechaCreacion": "2024-01-01T12:00:00"
  }
]
```

#### Create Task
```http
POST /api/v1/tareas
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Fix login bug",
  "descripcion": "Fix issue where login fails with special characters",
  "prioridad": "MEDIUM",
  "estado": "PENDING",
  "fechaVencimiento": "2024-01-20"
}
```

**Required Role:** USER or ADMIN

**Validation Rules:**
- `titulo` (title): Required, 1-200 characters
- `descripcion` (description): Optional, max 1000 characters
- `prioridad` (priority): Required, must be LOW, MEDIUM, HIGH, or CRITICAL
- `estado` (status): Required, must be PENDING, IN_PROGRESS, COMPLETED, or CANCELLED
- `fechaVencimiento` (due date): Required, must be future date (ISO 8601 format)

**Response (201 Created):**
```json
{
  "id": 2,
  "titulo": "Fix login bug",
  "descripcion": "Fix issue where login fails with special characters",
  "estado": "PENDING",
  "prioridad": "MEDIUM",
  "usuarioId": 1,
  "fechaVencimiento": "2024-01-20",
  "fechaCreacion": "2024-01-10T10:30:00"
}
```

#### Get Task by ID
```http
GET /api/v1/tareas/{id}
Authorization: Bearer {token}
```

**Required Role:** ADMIN or USER (must own the task unless ADMIN)

**Response (200 OK):**
```json
{
  "id": 1,
  "titulo": "Complete project documentation",
  "descripcion": "Write comprehensive API documentation",
  "estado": "IN_PROGRESS",
  "prioridad": "HIGH",
  "usuarioId": 1,
  "fechaVencimiento": "2024-01-15",
  "fechaCreacion": "2024-01-01T12:00:00"
}
```

#### Update Task
```http
PUT /api/v1/tareas/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Complete project documentation - Updated",
  "estado": "COMPLETED",
  "prioridad": "HIGH"
}
```

**Required Role:** USER or ADMIN

**Response (200 OK):**
```json
{
  "id": 1,
  "titulo": "Complete project documentation - Updated",
  "descripcion": "Write comprehensive API documentation",
  "estado": "COMPLETED",
  "prioridad": "HIGH",
  "usuarioId": 1,
  "fechaVencimiento": "2024-01-15",
  "fechaCreacion": "2024-01-01T12:00:00"
}
```

#### Delete Task
```http
DELETE /api/v1/tareas/{id}
Authorization: Bearer {token}
```

**Required Role:** USER or ADMIN

**Response (204 No Content)**

#### Get User's Tasks
```http
GET /api/v1/tareas/usuario/{usuarioId}
Authorization: Bearer {token}
```

**Required Role:** ADMIN or USER

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "titulo": "Task 1",
    "estado": "IN_PROGRESS",
    "prioridad": "HIGH",
    "usuarioId": 1
  },
  {
    "id": 2,
    "titulo": "Task 2",
    "estado": "PENDING",
    "prioridad": "MEDIUM",
    "usuarioId": 1
  }
]
```

---

## Error Handling

### Error Response Format (RFC 7807)

All error responses follow the RFC 7807 "Problem Details for HTTP APIs" standard:

```json
{
  "type": "https://example.com/probs/out-of-credit",
  "title": "You do not have enough credit.",
  "status": 400,
  "detail": "Your current balance is 30, but that costs 50.",
  "instance": "/account/12345/msgs/abc",
  "errorCode": "INSUFFICIENT_CREDIT",
  "timestamp": "2024-01-10T12:00:00.000Z",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "errors": {
    "fieldName": ["Field-specific error message"]
  }
}
```

### Error Response Fields

| Field      | Type                 | Description                                              |
|------------|----------------------|----------------------------------------------------------|
| `type`     | String (URI)         | Identifies the error type (problem class reference URL)  |
| `title`    | String               | Short, human-readable error title                        |
| `status`   | Integer              | HTTP status code                                         |
| `detail`   | String               | Human-readable explanation of the error                  |
| `instance` | String (URI)         | Identifies the specific occurrence (request path)        |
| `errorCode`| String               | Machine-readable error code for programmatic handling    |
| `timestamp`| ISO 8601 DateTime    | When the error occurred (UTC)                           |
| `traceId`  | UUID                 | Unique request trace ID for log correlation             |
| `errors`   | Map<String, List>    | Field-level validation errors (validation errors only)  |

### Error Codes

| Error Code                    | HTTP Status | Description                                    |
|-------------------------------|-------------|------------------------------------------------|
| `RESOURCE_NOT_FOUND`          | 404         | Requested resource does not exist              |
| `BUSINESS_CONFLICT`           | 409         | Business rule violation (e.g., duplicate)      |
| `VALIDATION_ERROR`            | 400         | Input validation failed                        |
| `UNAUTHORIZED`                | 401         | Authentication failed or credentials invalid   |
| `FORBIDDEN`                   | 403         | Authenticated but not authorized               |
| `INTERNAL_SERVER_ERROR`       | 500         | Unexpected server-side error                   |
| `INVALID_JWT_TOKEN`           | 401         | JWT token is invalid or expired                |
| `INVALID_CREDENTIALS`         | 401         | Username/password credentials are invalid      |

### Common Error Scenarios

#### Invalid JWT Token
```http
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Invalid or expired JWT token",
  "instance": "/api/v1/usuarios",
  "errorCode": "INVALID_JWT_TOKEN",
  "timestamp": "2024-01-10T12:00:00.000Z",
  "traceId": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### Missing Authorization Header
```http
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Authorization header is missing",
  "instance": "/api/v1/usuarios",
  "errorCode": "UNAUTHORIZED",
  "timestamp": "2024-01-10T12:00:00.000Z",
  "traceId": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### Insufficient Permissions
```http
HTTP/1.1 403 Forbidden
Content-Type: application/json

{
  "type": "about:blank",
  "title": "Forbidden",
  "status": 403,
  "detail": "User role 'USER' does not have permission to perform this action",
  "instance": "/api/v1/usuarios",
  "errorCode": "FORBIDDEN",
  "timestamp": "2024-01-10T12:00:00.000Z",
  "traceId": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### Business Rule Violation
```http
HTTP/1.1 409 Conflict
Content-Type: application/json

{
  "type": "about:blank",
  "title": "Conflict",
  "status": 409,
  "detail": "User with email 'john@example.com' already exists",
  "instance": "/api/v1/usuarios",
  "errorCode": "BUSINESS_CONFLICT",
  "timestamp": "2024-01-10T12:00:00.000Z",
  "traceId": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### Validation Errors (Multiple Fields)
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Input validation failed",
  "instance": "/api/v1/usuarios",
  "errorCode": "VALIDATION_ERROR",
  "timestamp": "2024-01-10T12:00:00.000Z",
  "traceId": "550e8400-e29b-41d4-a716-446655440000",
  "errors": {
    "email": [
      "must be a well-formed email address",
      "Email must be valid"
    ],
    "password": [
      "Password must contain at least one uppercase letter",
      "Password must contain at least one special character"
    ],
    "nombre": [
      "must not be blank"
    ]
  }
}
```

---

## Logging & Tracing

### Distributed Trace IDs

The application automatically implements distributed tracing through trace IDs. Each HTTP request receives a unique trace ID that propagates through all system components:

#### Request Flow
1. **Incoming Request:** If no `X-Trace-Id` header is present, the application generates a new UUID
2. **Header Extraction:** The `TraceIdInterceptor` extracts or creates the trace ID
3. **MDC Population:** SLF4J MDC (Mapped Diagnostic Context) is populated with trace ID and request metadata
4. **Request Processing:** All log messages automatically include the trace ID
5. **Response Header:** The trace ID is included in the response header for client reference
6. **Log Output:** Logs are written with the trace ID embedded in the pattern

#### Trace ID Example Request/Response
```http
GET /api/v1/usuarios HTTP/1.1
Authorization: Bearer eyJ...
X-Trace-Id: 550e8400-e29b-41d4-a716-446655440000

HTTP/1.1 200 OK
X-Trace-Id: 550e8400-e29b-41d4-a716-446655440000
Content-Type: application/json

[...]
```

### Log Format

Logs include the following information:

```
2024-01-10 12:00:45.123 [main] INFO com.riwi.catalog.application.usecase.UsuarioUseCase - [TraceId: 550e8400-e29b-41d4-a716-446655440000] [UserId: 1] Fetching user with ID: 1 - Execution time: 25ms
```

#### Format Components
- **Timestamp:** `2024-01-10 12:00:45.123` (ISO 8601 format)
- **Thread:** `[main]` - Thread name
- **Log Level:** `INFO` - DEBUG, INFO, WARN, ERROR
- **Logger:** `com.riwi.catalog.application.usecase.UsuarioUseCase` - Class name
- **Trace ID:** `[TraceId: 550e8400-e29b-41d4-a716-446655440000]` - Unique request ID
- **User ID:** `[UserId: 1]` - Authenticated user (from JWT)
- **Message:** Actual log content
- **Timing:** Execution time for operations

### Log Levels

| Level | Usage                                                    |
|-------|----------------------------------------------------------|
| DEBUG | Detailed information for debugging (development only)   |
| INFO  | General informational messages (application flow)       |
| WARN  | Warning messages (potential issues)                     |
| ERROR | Error messages (exceptions and failures)                |

### Log Configuration

The application uses Logback with configuration in `logback-spring.xml`:

- **Console Appender:** Color-coded output for development
- **File Appender:** Rolling file with async processing
- **Async Appender:** Non-blocking logging for performance
- **Rolling Policy:** 
  - Maximum file size: 10 MB
  - Maximum history: 30 days
  - Total size cap: 1 GB

### Accessing Logs

```bash
# View real-time logs
tail -f logs/application.log

# Search logs by trace ID
grep "550e8400-e29b-41d4-a716-446655440000" logs/application.log

# Search logs by user ID
grep "UserId: 1" logs/application.log

# Search logs by error level
grep "ERROR" logs/application.log
```

### MDC Context Properties

| Property      | Type   | Description                    |
|---------------|--------|--------------------------------|
| `traceId`     | String | Unique request trace ID (UUID) |
| `userId`      | String | Authenticated user ID          |
| `requestPath` | String | Request URI path               |
| `httpMethod`  | String | HTTP method (GET, POST, etc.)  |

---

## Security & Authorization

### Security Configuration

The application implements Spring Security with JWT-based stateless authentication:

#### Key Security Features
- **CSRF Protection:** Disabled (stateless API)
- **Session Management:** STATELESS (no server-side sessions)
- **Authentication:** JWT Bearer tokens
- **Authorization:** Role-based access control (RBAC)
- **Password Encoding:** BCrypt with strength 10

### Role-Based Access Control (RBAC)

#### ADMIN Role
- Create, read, update, delete users
- Create, read, update, delete any task
- Access all audit and monitoring endpoints

#### USER Role
- Read own user profile
- Create, read, update, delete own tasks
- Cannot perform administrative operations

### Endpoint Authorization Matrix

| Endpoint                      | Method | ADMIN | USER | Public |
|-------------------------------|--------|-------|------|--------|
| `/api/v1/health`              | GET    | ✓     | ✓    | ✓      |
| `/api/v1/auth/login`          | POST   | ✓     | ✓    | ✓      |
| `/api/v1/usuarios`            | GET    | ✓     | ✓    | ✗      |
| `/api/v1/usuarios`            | POST   | ✓     | ✗    | ✗      |
| `/api/v1/usuarios/{id}`       | GET    | ✓     | ✓    | ✗      |
| `/api/v1/usuarios/{id}`       | PUT    | ✓     | ✗    | ✗      |
| `/api/v1/usuarios/{id}`       | DELETE | ✓     | ✗    | ✗      |
| `/api/v1/tareas`              | GET    | ✓     | ✓    | ✗      |
| `/api/v1/tareas`              | POST   | ✓     | ✓    | ✗      |
| `/api/v1/tareas/{id}`         | GET    | ✓     | ✓    | ✗      |
| `/api/v1/tareas/{id}`         | PUT    | ✓     | ✓    | ✗      |
| `/api/v1/tareas/{id}`         | DELETE | ✓     | ✓    | ✗      |
| `/api/v1/tareas/usuario/{id}` | GET    | ✓     | ✓    | ✗      |
| `/h2-console/**`              | *      | ✓     | ✗    | ✗      |

### Password Requirements

Strong passwords must meet the following criteria:

- **Minimum Length:** 8 characters
- **Uppercase Letters:** At least one (A-Z)
- **Lowercase Letters:** At least one (a-z)
- **Digits:** At least one (0-9)
- **Special Characters:** At least one (!@#$%^&*)

**Valid Example:** `MyPassword123!`
**Invalid Example:** `password123` (no uppercase, no special char)

### JWT Token Details

#### Token Generation
```
Header.Payload.Signature
```

#### Payload Structure
```json
{
  "sub": "admin",
  "role": "ADMIN",
  "iat": 1705008000,
  "exp": 1705094400
}
```

#### Claims
- **sub (subject):** Username
- **role:** User role (ADMIN or USER)
- **iat (issued at):** Token creation timestamp
- **exp (expiration):** Token expiration timestamp

#### Token Validation
Tokens are validated on each request for:
1. **Signature:** Must be signed with the application secret
2. **Expiration:** Must not be past the expiration time
3. **Format:** Must be valid JWT format (3 parts separated by dots)

### Security Best Practices

#### Development Environment
- Use test credentials (admin/admin123, user/user123) for local testing
- Enable H2 console at `/h2-console` for database inspection
- Set JWT secret via environment variable (minimum 256 bits)

#### Production Deployment
- **Change Default Credentials:** Remove test credentials from `AuthController`
- **Secure JWT Secret:** Use environment variable with secure, random 256+ bit key
  ```bash
  export JWT_SECRET="your-production-secret-key-with-256-bits-minimum"
  ```
- **HTTPS Only:** Always use HTTPS in production
- **CORS Configuration:** Configure CORS policies appropriately
- **Rate Limiting:** Implement rate limiting on authentication endpoints
- **Audit Logging:** Enable and monitor security-related logs
- **Token Refresh:** Implement token refresh mechanism for extended sessions
- **Database:** Migrate from H2 to production database (MySQL, PostgreSQL)

---

## Database Schema

### Entity Relationships

```
User (1) ──────────── (N) Task
  │
  ├─ id (PK)
  ├─ nombre
  ├─ email (UNIQUE)
  ├─ password (hashed)
  ├─ rol
  └─ activo

Task
  │
  ├─ id (PK)
  ├─ titulo
  ├─ descripcion
  ├─ estado
  ├─ prioridad
  ├─ usuarioId (FK → User.id)
  ├─ fechaVencimiento
  └─ fechaCreacion
```

### Enumerations

#### User Role (rol)
- `ADMIN` - Administrator with full access
- `USER` - Regular user with limited access

#### Task Status (estado)
- `PENDING` - Task not started
- `IN_PROGRESS` - Task currently being worked on
- `COMPLETED` - Task finished successfully
- `CANCELLED` - Task cancelled and will not be completed

#### Task Priority (prioridad)
- `LOW` - Low priority
- `MEDIUM` - Medium priority
- `HIGH` - High priority
- `CRITICAL` - Critical/urgent priority

### Database Migration

Flyway automatically manages database schema versions:

```
src/main/resources/db/migration/
├── V1__Initial_schema.sql
├── V2__Add_constraints.sql
└── V3__Add_indexes.sql
```

---

## Configuration

### Environment Variables

The application supports the following environment variables:

```bash
# JWT Configuration
JWT_SECRET=your-256-bit-minimum-secret-key

# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/catalog
DB_USERNAME=root
DB_PASSWORD=password

# Application Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=production
```

### Application Properties

Key configuration in `application.properties`:

```properties
# Application
spring.application.name=catalog
server.port=8080

# Database (H2 for development)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=validate

# JWT
jwt.secret=${JWT_SECRET:default-secret-key}
jwt.expirationMs=86400000

# Logging
logging.level.com.riwi.catalog=DEBUG
logging.level.org.springframework=INFO
logging.file.name=logs/application.log
```

### Spring Profiles

#### Development Profile
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

#### Production Profile
```bash
java -jar target/catalog-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## Development Guidelines

### Project Structure

Follow hexagonal architecture principles:

```
application/
  ├─ entity/        # Domain entities
  ├─ dto/           # Data transfer objects
  ├─ mapper/        # Entity/DTO converters
  ├─ repository/    # Data access abstraction
  └─ usecase/       # Business logic

infrastructure/
  ├─ controller/    # HTTP endpoints
  ├─ exception/     # Error handling
  ├─ logging/       # Observability
  ├─ security/      # Authentication/authorization
  └─ validation/    # Custom validators
```

### Code Style

#### Naming Conventions
- **Classes:** PascalCase (e.g., `UsuarioCreateDTO`)
- **Methods:** camelCase (e.g., `createUser`)
- **Constants:** UPPER_SNAKE_CASE (e.g., `DEFAULT_TIMEOUT`)
- **Packages:** lowercase.domain.layer

#### Exception Handling
Always use custom exceptions from `infrastructure.exception`:
```java
if (usuarioId <= 0) {
    throw new ValidationException("Usuario ID must be positive", "INVALID_ID");
}

if (!usuarioRepository.existsById(id)) {
    throw new ResourceNotFoundException("User with ID " + id + " not found", "USER_NOT_FOUND");
}
```

#### Logging
Use SLF4J with automatic trace context:
```java
@Slf4j
public class UsuarioUseCase {
    public Usuario crearUsuario(UsuarioCreateDTO dto) {
        log.info("Creating new user with email: {}", dto.getEmail());
        // Implementation
        log.debug("User created with ID: {}", usuario.getId());
    }
}
```

#### Validation
Use custom validators for domain rules:
```java
@Data
public class UsuarioCreateDTO {
    @NotBlank(message = "Email is required")
    @ValidEmail  // Custom validator
    private String email;
    
    @NotBlank(message = "Password is required")
    @StrongPassword  // Custom validator
    private String password;
}
```

### Testing

#### Unit Tests
```java
@SpringBootTest
class UsuarioUseCaseTests {
    @InjectMocks
    private UsuarioUseCase usuarioUseCase;
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Test
    void testCreateUsuario() {
        // Arrange
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        
        // Act
        Usuario result = usuarioUseCase.crear(dto);
        
        // Assert
        assertNotNull(result);
        verify(usuarioRepository, times(1)).save(any());
    }
}
```

#### Integration Tests
```java
@SpringBootTest
@ActiveProfiles("test")
class UsuarioIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testLoginEndpoint() {
        LoginRequest request = new LoginRequest("admin", "admin123");
        ResponseEntity<AuthResponse> response = 
            restTemplate.postForEntity("/api/v1/auth/login", request, AuthResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getToken());
    }
}
```

### Building & Deployment

#### Local Build
```bash
./mvnw clean install
```

#### Docker Build
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/catalog-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

#### Run with Docker
```bash
docker build -t catalog:latest .
docker run -e JWT_SECRET="your-secret" -p 8080:8080 catalog:latest
```

---

## Testing

### Running Tests

#### All Tests
```bash
./mvnw test
```

#### Specific Test Class
```bash
./mvnw test -Dtest=UsuarioUseCaseTests
```

#### Specific Test Method
```bash
./mvnw test -Dtest=UsuarioUseCaseTests#testCreateUsuario
```

### Test Endpoints

#### Login and Get Token
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

#### Sample Response
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTcwMzQzMjAwMCwiZXhwIjoxNzAzNTE4NDAwfQ.signature",
  "username": "admin",
  "role": "ADMIN",
  "expiresIn": 86400
}
```

#### Use Token to Access Protected Endpoint
```bash
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

curl -X GET http://localhost:8080/api/v1/usuarios \
  -H "Authorization: Bearer $TOKEN"
```

### Testing with Postman

1. **Create Login Request**
   - Method: POST
   - URL: `http://localhost:8080/api/v1/auth/login`
   - Body (JSON):
     ```json
     {
       "username": "admin",
       "password": "admin123"
     }
     ```

2. **Save Token**
   - Copy token from response
   - Set variable: `{{authToken}}`

3. **Access Protected Endpoint**
   - Method: GET
   - URL: `http://localhost:8080/api/v1/usuarios`
   - Header: `Authorization: Bearer {{authToken}}`

---

## Troubleshooting

### Common Issues

#### JWT Token Expired
**Error:** `Invalid or expired JWT token`
**Solution:** Request a new token via login endpoint

#### Insufficient Permissions
**Error:** `User role 'USER' does not have permission`
**Solution:** Use admin credentials or ensure user has required role

#### Database Connection Error
**Error:** `Cannot connect to database`
**Solution:** Verify database is running and connection properties are correct

#### Validation Errors
**Error:** `Validation failed` with field-level errors
**Solution:** Check error details in response and correct invalid fields

### Debug Mode

Enable debug logging:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--debug"
```

View H2 Database Console:
- URL: `http://localhost:8080/h2-console`
- Driver: `org.h2.Driver`
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (leave empty)

---

## Contributing

### Branch Structure

```
main (production)
├── develop (staging)
│   ├── feature/error-handling-validation
│   ├── feature/observability-logging
│   └── feature/security-jwt
└── HU-semana5 (container branch)
    ├── feature/error-handling-validation
    ├── feature/observability-logging
    └── feature/security-jwt
```

### Commit Messages

Follow conventional commits:
```
feat: Add new feature
fix: Fix a bug
docs: Update documentation
refactor: Refactor code without changing behavior
test: Add or update tests
chore: Update dependencies or configuration
```

Example:
```
feat(auth): Add JWT token provider with HS256 algorithm
docs: Add comprehensive API documentation
fix(validation): Fix email validation pattern
```

### Pull Request Process

1. Create feature branch from `develop`
2. Implement changes with tests
3. Submit PR with description
4. Request code review
5. Address feedback
6. Merge after approval

---

## License

This project is licensed under the MIT License - see `LICENSE` file for details.

---

## Contact & Support

For questions or support:
- **GitHub Issues:** [catalog/issues](https://github.com/And-Anillo/catalog/issues)
- **Email:** support@example.com

---

## Changelog

### Version 1.0.0 (2024-01-10)

#### Sprint 4 Features
- User management with role-based access
- Task management with full CRUD operations
- JPA relationships and cascading operations
- Optimized transactions and isolation levels

#### Sprint 5 Features
- RFC 7807 compliant error handling
- Custom validation annotations
- Distributed tracing with trace IDs
- SLF4J MDC integration
- Asynchronous logging
- Spring Security integration
- JWT-based authentication
- Role-based access control

---

**Last Updated:** January 10, 2024
**Maintained by:** Catalog Development Team
**Repository:** https://github.com/And-Anillo/catalog
