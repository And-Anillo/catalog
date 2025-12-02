# Catalog - Administración de Tareas por Usuario

## 📋 Descripción

Sistema de gestión de tareas construido con **Spring Boot 3.5.7** implementando una arquitectura hexagonal con soporte para relaciones avanzadas de JPA/Hibernate, transacciones optimizadas y migraciones versionadas con Flyway.

## 🎯 Características Principales

### ✅ Task 1: Relaciones JPA Avanzadas
- **OneToMany**: Usuario → Tarea (con orphanRemoval y cascade)
- **ManyToOne**: Tarea → Usuario, Tarea → Categoria
- **ManyToMany**: Usuario ↔ Categoria
- **Lazy Loading**: Estrategia por defecto para evitar sobrecarga
- **Ciclo de Vida**: PERSIST, MERGE, DETACH, REMOVE

### ✅ Task 2: Optimización de Consultas
- **Specifications**: Filtros dinámicos reutilizables
- **JPQL con JOIN FETCH**: Eliminación de N+1 queries
- **@EntityGraph**: Eager loading controlado
- **Batch Size**: Agrupamiento de lazy loading (batchSize=10)
- **Paginación**: Para consultas grandes
- **Índices Optimizados**: En campos frecuentemente consultados

### ✅ Task 3: Transaccionalidad y Flyway
- **@Transactional**: Con propagación REQUIRED/REQUIRES_NEW
- **ReadOnly**: Optimización para búsquedas
- **Migraciones Versionadas**: V1, V2, V3 con Flyway
- **Datos de Prueba**: Incluidos en V3__test_data.sql

## 📦 Estructura del Proyecto

```
src/main/java/com/riwi/catalog/
├── entity/                          # Entidades del dominio
│   ├── Usuario.java
│   ├── Tarea.java
│   └── Categoria.java
├── repository/                      # Capa de persistencia
│   ├── UsuarioRepository.java
│   ├── TareaRepository.java
│   ├── CategoriaRepository.java
│   └── specification/               # Specifications para filtros
│       ├── TareaSpecifications.java
│       ├── UsuarioSpecifications.java
│       └── CategoriaSpecifications.java
├── application/
│   ├── usecase/                     # Casos de uso
│   │   ├── BuscarTareasUseCase.java
│   │   ├── BuscarUsuariosUseCase.java
│   │   ├── CrearActualizarUsuarioUseCase.java
│   │   └── CrearActualizarTareaUseCase.java
│   └── dto/                         # Data Transfer Objects
│       └── TareaFilterDTO.java
└── infrastructure/
    └── controller/                  # Controladores REST
        └── CatalogController.java

src/main/resources/
├── db/migration/                    # Migraciones Flyway
│   ├── V1__init.sql
│   ├── V2__relaciones.sql
│   └── V3__test_data.sql
└── application.properties           # Configuración
```

## 🚀 Uso Rápido

### Prerrequisitos
- Java 17+
- Maven 3.8+
- H2 Database (incluido)

### Instalación

```bash
# Clonar repositorio
git clone https://github.com/And-Anillo/catalog.git
cd catalog

# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

### Acceder a la Aplicación

- **API REST**: http://localhost:8080/api/v1
- **H2 Console**: http://localhost:8080/h2-console
  - URL: jdbc:h2:mem:catalog;MODE=MySQL
  - Usuario: sa
  - Contraseña: (dejar vacío)

## 📡 API Endpoints

### Usuarios

```bash
# Obtener usuario con relaciones
GET /api/v1/usuarios/{id}

# Listar usuarios activos
GET /api/v1/usuarios

# Crear usuario
POST /api/v1/usuarios
Content-Type: application/json
{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "descripcion": "Desarrollador",
  "activo": true
}

# Actualizar usuario
PUT /api/v1/usuarios/{id}
```

### Tareas

```bash
# Obtener tarea con relaciones
GET /api/v1/tareas/{id}

# Obtener tareas de un usuario (JOIN FETCH)
GET /api/v1/usuarios/{usuarioId}/tareas

# Obtener tareas por estado (Specifications)
GET /api/v1/tareas/estado/{estado}

# Obtener tareas vencidas
GET /api/v1/tareas/vencidas/{usuarioId}

# Obtener tareas próximas a vencer
GET /api/v1/tareas/proximas-vencer/{usuarioId}?dias=7

# Crear tarea
POST /api/v1/usuarios/{usuarioId}/tareas
Content-Type: application/json
{
  "titulo": "Nueva tarea",
  "descripcion": "Descripción",
  "estado": "PENDIENTE",
  "prioridad": "ALTA",
  "fechaVencimiento": "2025-12-31T23:59:59"
}

# Actualizar tarea
PUT /api/v1/tareas/{id}

# Cambiar estado
PATCH /api/v1/tareas/{id}/estado?estado=COMPLETADA

# Reasignar tarea
PATCH /api/v1/tareas/{id}/reasignar?nuevoUsuarioId={id}
```

## 📊 Ejemplos de Búsqueda Avanzada

### Con Specifications

```java
// Buscar tareas de un usuario, pendientes, vencidas
Specification<Tarea> spec = TareaSpecifications
    .conUsuarioId(1L)
    .and(TareaSpecifications.conEstado(EstadoTarea.PENDIENTE))
    .and(TareaSpecifications.conFechaVencimientoPasada());

Page<Tarea> resultados = tareaRepository.findAll(spec, pageable);
```

### Con Paginación

```java
Pageable pageable = PageRequest.of(
    0,                                              // página
    10,                                             // tamaño
    Sort.by("prioridad").descending()
        .and(Sort.by("fechaVencimiento").ascending())
);

Page<Tarea> tareas = tareaRepository.findAll(spec, pageable);
```

## 🔄 Migraciones Flyway

### Ver historial de migraciones

```sql
SELECT * FROM flyway_schema_history;
```

### V1__init.sql - Esquema Inicial
- Crea tablas: usuario, tarea, categoria, usuario_categoria
- Índices simples
- Foreign keys con ON DELETE CASCADE

### V2__relaciones.sql - Constraints Avanzados
- Composite indexes para queries frecuentes
- CHECK constraints
- Documentación de tablas

### V3__test_data.sql - Datos de Prueba
- 4 usuarios, 5 categorías, 13 tareas
- Diferentes estados y prioridades
- Relaciones usuario-categoría

## ⚙️ Configuración

### application.properties

```properties
# Base de datos H2
spring.datasource.url=jdbc:h2:mem:catalog;MODE=MySQL
spring.datasource.driverClassName=org.h2.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.default_batch_fetch_size=10

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# Logging
logging.level.org.hibernate.SQL=DEBUG
```

## 🎓 Conceptos Implementados

### Lazy vs Eager Loading

```java
// LAZY (por defecto) - mejor para evitar sobrecarga
@OneToMany(fetch = FetchType.LAZY)
Set<Tarea> tareas;

// EAGER con @EntityGraph - solo cuando necesites
@EntityGraph(attributePaths = {"tareas", "categorias"})
Optional<Usuario> findWithRelationsById(Long id);
```

### Cascade Strategies

```java
// PERSIST + MERGE - seguir ciclo de vida
cascade = {CascadeType.PERSIST, CascadeType.MERGE}

// orphanRemoval - eliminar hijos sin padre
orphanRemoval = true
```

### Propagation Levels

```java
// REQUIRED - usa transacción existente o crea nueva (por defecto)
@Transactional(propagation = Propagation.REQUIRED)

// REQUIRES_NEW - siempre crea nueva, suspende la anterior
@Transactional(propagation = Propagation.REQUIRES_NEW)
```

## 📚 Documentación Completa

- **[OPTIMIZATION.md](OPTIMIZATION.md)** - Estrategias de optimización de consultas
- **[TRANSACTIONAL.md](TRANSACTIONAL.md)** - Configuración de transacciones y Flyway
- **[HU_SEMANA4_SUMMARY.md](HU_SEMANA4_SUMMARY.md)** - Resumen completo de la Historia de Usuario

## 🔗 Estructura de Ramas

```
develop
└── HU-semana4
    ├── feature/jpa-relationships      (Task 1)
    ├── feature/query-optimization     (Task 2)
    └── feature/flyway-transactions    (Task 3)
```

## 🧪 Testing

### Migraciones
```bash
# Las migraciones se ejecutan automáticamente en startup
# Ver logs: SQL de Flyway al iniciar aplicación
```

### Búsquedas Optimizadas
```bash
# Habilitar debug SQL
logging.level.org.hibernate.SQL=DEBUG

# Verificar queries generadas en logs
```

### Transacciones
```bash
# Debug de transacciones
logging.level.org.springframework.transaction=DEBUG
```

## 📝 Dependencias Principales

```xml
<!-- Spring Boot -->
<spring-boot-version>3.5.7</spring-boot-version>

<!-- Data JPA -->
<spring-boot-starter-data-jpa>

<!-- Flyway -->
<flyway-core>
<flyway-mysql>

<!-- Lombok -->
<lombok>

<!-- H2 Database -->
<h2>
```

## 🤝 Contribuir

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/mi-feature`)
3. Commit tus cambios (`git commit -am 'Agregar feature'`)
4. Push a la rama (`git push origin feature/mi-feature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la licencia MIT.

## 👨‍💻 Autor

- **And-Anillo** - [GitHub](https://github.com/And-Anillo)

## 🆘 Soporte

Para reportar issues o sugerencias:
- GitHub Issues: https://github.com/And-Anillo/catalog/issues

---

**Última actualización**: Diciembre 2, 2025
**Versión**: 0.0.1-SNAPSHOT
**Estado**: ✅ Producción (Historia de Usuario HU-semana4 completada)
