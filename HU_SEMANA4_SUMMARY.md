# Historia de Usuario - Administración de Tareas por Usuario con Relaciones y Transacciones Optimizadas

## Estado: ✅ COMPLETADA

### Objetivo
Optimizar el acceso y la persistencia de datos dentro de la arquitectura hexagonal, aplicando relaciones avanzadas con JPA/Hibernate, control de transacciones y consultas eficientes mediante JPQL y Specifications. Además, incorporar migraciones versionadas para mantener la base de datos sincronizada entre entornos.

### Estructura de Ramas

```
develop
└── HU-semana4  (Container for the entire User Story 4)
    ├── feature/jpa-relationships    (Task 1) ✅
    ├── feature/query-optimization   (Task 2) ✅
    └── feature/flyway-transactions  (Task 3) ✅
```

---

## TASK 1: Relaciones y Ciclo de Vida de Entidades ✅

### Descripción
Implementar relaciones OneToMany, ManyToOne y ManyToMany entre entidades como Usuario y Tarea, configurando correctamente las propiedades de relación (mappedBy, cascade, orphanRemoval, etc.). Analizar y aplicar estrategias de carga Lazy y Eager, priorizando Lazy para evitar sobrecarga. Revisar el ciclo de vida de las entidades.

### Entidades Creadas

#### 1. Usuario
- **Relaciones**:
  - OneToMany con Tarea (mappedBy="usuario", cascade=PERSIST/MERGE, orphanRemoval=true)
  - ManyToMany con Categoria (JoinTable="usuario_categoria")
- **Propiedades**:
  - Lazy loading en tareas y categorías
  - PrePersist y PreUpdate para timestamps
  - Métodos helper: addTarea(), removeTarea(), addCategoria(), removeCategoria()

#### 2. Tarea
- **Relaciones**:
  - ManyToOne con Usuario (fetch=LAZY)
  - ManyToOne con Categoria (fetch=LAZY)
- **Enumeraciones**:
  - EstadoTarea: PENDIENTE, EN_PROGRESO, COMPLETADA, CANCELADA
  - Prioridad: BAJA, MEDIA, ALTA, URGENTE
- **Propiedades**:
  - PrePersist y PreUpdate para timestamps
  - Índices en usuario_id, estado, fecha_vencimiento

#### 3. Categoria
- **Relaciones**:
  - OneToMany con Tarea (mappedBy="categoria", cascade=PERSIST/MERGE)
  - ManyToMany con Usuario (mappedBy="categorias")
- **Propiedades**:
  - Color CSS (ej: #FF6B6B)
  - Soft delete vía campo "activo"

### Configuración de Cascade

| Propiedad | Valor | Razón |
|-----------|-------|-------|
| cascade | {PERSIST, MERGE} | Guardar/actualizar entidades relacionadas |
| orphanRemoval | true | Eliminar tareas sin usuario |
| fetch | LAZY | Cargar bajo demanda |

### Ciclo de Vida Implementado

1. **PERSIST**: Usuario nuevo con tareas se guardan juntos
2. **MERGE**: Cambios en usuario/tareas se sincronizan
3. **DETACH**: Fin de transacción, objeto sale del contexto
4. **REMOVE**: Eliminación de usuario también elimina tareas

### Rama: `feature/jpa-relationships`
- ✅ Entidades creadas
- ✅ Repositorios definidos
- ✅ Cascadas configuradas
- ✅ Índices en BD

---

## TASK 2: Optimización de Consultas ✅

### Descripción
Reemplazar consultas nativas o ineficientes por JPQL y Specifications. Implementar filtros dinámicos, detectar y reducir el problema de N+1 queries mediante @EntityGraph, join fetch y configuración de fetchType y batchSize.

### Estrategias de Optimización Implementadas

#### 1. JPQL con JOIN FETCH
```java
@Query("SELECT t FROM Tarea t JOIN FETCH t.usuario u WHERE u.id = :usuarioId")
List<Tarea> findByUsuarioIdWithUsuario(@Param("usuarioId") Long usuarioId);
```
- **Beneficio**: Evita N+1 queries
- **Uso**: Cuando se necesitan relaciones asociadas

#### 2. @EntityGraph
```java
@EntityGraph(attributePaths = {"usuario", "categoria"})
Optional<Tarea> findWithRelationsById(Long id);
```
- **Beneficio**: Eager loading controlado
- **Uso**: Obtener entidad con relaciones específicas

#### 3. Specifications para Filtros Dinámicos
```java
Specification<Tarea> spec = TareaSpecifications.conUsuarioId(usuarioId)
    .and(TareaSpecifications.conEstado(EstadoTarea.PENDIENTE))
    .and(TareaSpecifications.conFechaVencimientoPasada());
```
- **Beneficio**: Composición reutilizable de filtros
- **Uso**: Búsquedas avanzadas flexibles

#### 4. Batch Fetch Size
```properties
spring.jpa.properties.hibernate.default_batch_fetch_size=10
```
- **Beneficio**: Agrupa lazy loading en batches
- **Ejemplo**: 100 usuarios + tareas = 1 + 10 queries (vs 1 + 100)

#### 5. Paginación
```java
Pageable pageable = PageRequest.of(page, size, Sort.by("prioridad").descending());
return tareaRepository.findAll(spec, pageable);
```
- **Beneficio**: Reduce memoria, mejora respuesta
- **Uso**: Listados grandes

#### 6. Índices en BD
- usuario.email, usuario.nombre
- tarea.usuario_id, tarea.estado, tarea.fecha_vencimiento
- Composite: (usuario_id, estado), (usuario_id, fecha_vencimiento)

### Use Cases para Búsqueda

#### BuscarTareasUseCase
- `obtenerTareaConRelaciones()`: @EntityGraph
- `obtenerTareasDelUsuario()`: JOIN FETCH
- `buscarTareasConFiltros()`: Specifications + Paginación
- `obtenerTareasVencidas()`: Composición de Specifications
- `obtenerTareasPorVencer()`: Rango de fechas

#### BuscarUsuariosUseCase
- `obtenerUsuarioConRelaciones()`: @EntityGraph
- `buscarPorNombre()`: Specifications + Paginación
- `obtenerActivosConTareas()`: Composición avanzada

### Clases de Specifications

#### TareaSpecifications
- conUsuarioId, conEstado, conCategoriaId, conPrioridad
- conFechaVencimientoBetween, conFechaVencimientoFutura/Pasada
- conTituloContiene, conDescripcionContiene
- sinCategoria, conCategoria

#### UsuarioSpecifications
- conNombreContiene, conEmailContiene
- esActivo, esInactivo
- conTareas, sinTareas

#### CategoriaSpecifications
- conNombreContiene
- esActiva, esInactiva
- conTareas, sinTareas
- conUsuarios

### Rama: `feature/query-optimization`
- ✅ Specifications creadas
- ✅ Use cases de búsqueda
- ✅ Repositorios con JpaSpecificationExecutor
- ✅ DTOs para filtros
- ✅ Documentación (OPTIMIZATION.md)

---

## TASK 3: Transaccionalidad y Migraciones con Flyway ✅

### Descripción
Aplicar la anotación @Transactional en los casos de uso de la capa de aplicación, diferenciando entre transacciones de lectura y de escritura. Implementar migraciones de base de datos con Flyway con scripts SQL versionados.

### @Transactional Configuration

#### Niveles de Propagación

| Tipo | Propagación | Uso | Métodos |
|------|-------------|-----|---------|
| **Lectura** | REQUIRED (readOnly=true) | Búsquedas seguras | BuscarTareasUseCase, BuscarUsuariosUseCase |
| **Escritura** | REQUIRED | CRUD normal | CrearActualizarUsuarioUseCase, CrearActualizarTareaUseCase |
| **Independiente** | REQUIRES_NEW | Eliminaciones atómicas | eliminarUsuario(), reasignarTarea() |

#### Use Cases con Transacciones

##### CrearActualizarUsuarioUseCase
```java
@Transactional(propagation = Propagation.REQUIRED)
public Usuario crearUsuario(Usuario usuario) { ... }

@Transactional(propagation = Propagation.REQUIRES_NEW)
public void eliminarUsuario(Long usuarioId) { ... }
```

##### CrearActualizarTareaUseCase
```java
@Transactional(propagation = Propagation.REQUIRED)
public Tarea crearTarea(Tarea tarea, Long usuarioId) { ... }

@Transactional(propagation = Propagation.REQUIRES_NEW)
public Tarea reasignarTarea(Long tareaId, Long nuevoUsuarioId) { ... }
```

##### BuscarTareasUseCase
```java
@Transactional(readOnly = true)
public Optional<Tarea> obtenerTareaConRelaciones(Long tareaId) { ... }
```

### Migraciones Flyway

#### V1__init.sql - Esquema Inicial
**Crea tablas**:
- usuario (id, nombre, email, descripcion, timestamps, activo)
- categoria (id, nombre, descripcion, color, timestamps, activo)
- tarea (id, titulo, descripcion, estado, prioridad, usuario_id, categoria_id, timestamps)
- usuario_categoria (usuario_id, categoria_id) - ManyToMany

**Índices**:
- usuario.email (UNIQUE)
- usuario.nombre
- tarea.usuario_id, tarea.estado, tarea.fecha_vencimiento
- categoria.nombre (UNIQUE)
- Composite indexes

**Constraints**:
- Foreign Keys con ON DELETE CASCADE
- Valores por defecto

#### V2__relaciones.sql - Constraints Avanzados
**Agrega**:
- Composite indexes para query patterns:
  - (usuario_id, estado)
  - (usuario_id, fecha_vencimiento)
  - (estado, prioridad)
- CHECK constraints para estado y prioridad
- Documentación de tablas

#### V3__test_data.sql - Datos de Prueba
**Inserta**:
- 4 usuarios (1 inactivo)
- 5 categorías
- 13 tareas con diferentes estados
- Relaciones usuario-categoría

**Propósito**:
- Pruebas de búsqueda y filtros
- Validación de relaciones
- Verificación de rendimiento

### Configuración Application Properties

```properties
# Flyway
spring.flyway.enabled=true
spring.flyway.baselineOnMigrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.outOfOrder=false

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Batch Loading
spring.jpa.properties.hibernate.default_batch_fetch_size=10

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Controlador de Demostración

#### CatalogController
Endpoints para pruebas:
- **GET** /api/v1/usuarios/{id} - Obtener con @EntityGraph
- **GET** /api/v1/usuarios/{usuarioId}/tareas - Obtener con JOIN FETCH
- **GET** /api/v1/tareas/vencidas/{usuarioId} - Specifications avanzadas
- **POST** /api/v1/usuarios/{usuarioId}/tareas - Crear con transacción
- **PATCH** /api/v1/tareas/{id}/reasignar - REQUIRES_NEW
- **GET** /api/v1/health - Verificar Flyway

### Rama: `feature/flyway-transactions`
- ✅ Use cases con @Transactional
- ✅ Migraciones V1, V2, V3
- ✅ Controlador REST
- ✅ Documentación (TRANSACTIONAL.md)

---

## Criterios de Aceptación ✅

- [x] **Relaciones OneToMany, ManyToOne y ManyToMany correctamente configuradas**
  - Usuario ↔ Tarea (OneToMany)
  - Usuario ↔ Categoria (ManyToMany)
  - Tarea ↔ Categoria (ManyToOne)

- [x] **Uso adecuado de Lazy/Eager y ausencia de problemas N+1**
  - Lazy loading por defecto
  - @EntityGraph para casos específicos
  - JOIN FETCH en JPQL
  - Batch size configurado

- [x] **Consultas optimizadas con JPQL o Specifications**
  - Specifications para filtros dinámicos
  - JPQL con JOIN FETCH
  - Paginación implementada
  - Índices en BD

- [x] **Correcta aplicación de @Transactional según la operación**
  - readOnly=true para búsquedas
  - REQUIRED para escritura
  - REQUIRES_NEW para eliminaciones

- [x] **Migraciones versionadas con Flyway y reproducibles**
  - V1__init.sql: Esquema inicial
  - V2__relaciones.sql: Constraints avanzados
  - V3__test_data.sql: Datos de prueba
  - Ejecutables en H2 y MySQL

- [x] **El dominio mantiene independencia**
  - Arquitectura hexagonal respetada
  - Use cases en application layer
  - Entidades con lógica de negocio
  - Repositorios como adaptadores

---

## Archivos Generados

### Entidades (src/main/java/com/riwi/catalog/entity/)
- Usuario.java
- Tarea.java
- Categoria.java

### Repositorios (src/main/java/com/riwi/catalog/repository/)
- UsuarioRepository.java
- TareaRepository.java
- CategoriaRepository.java
- specification/TareaSpecifications.java
- specification/UsuarioSpecifications.java
- specification/CategoriaSpecifications.java

### Use Cases (src/main/java/com/riwi/catalog/application/usecase/)
- BuscarTareasUseCase.java
- BuscarUsuariosUseCase.java
- CrearActualizarUsuarioUseCase.java
- CrearActualizarTareaUseCase.java

### DTOs (src/main/java/com/riwi/catalog/application/dto/)
- TareaFilterDTO.java

### Controladores (src/main/java/com/riwi/catalog/infrastructure/controller/)
- CatalogController.java

### Migraciones (src/main/resources/db/migration/)
- V1__init.sql
- V2__relaciones.sql
- V3__test_data.sql

### Configuración
- application.properties (JPA, Flyway, Logging)
- pom.xml (Dependencies: Flyway)

### Documentación
- OPTIMIZATION.md
- TRANSACTIONAL.md
- HU_SEMANA4_SUMMARY.md

---

## Commits Realizados

### Rama: feature/jpa-relationships
```
commit ef60e05
TASK 1: Implementar relaciones JPA (OneToMany, ManyToOne, ManyToMany) con entidades Usuario, Tarea y Categoria
```

### Rama: feature/query-optimization
```
commit a3e97fd
TASK 2: Optimización de consultas con Specifications, JPQL, @EntityGraph y paginación
```

### Rama: feature/flyway-transactions
```
commit 87a6ab4
TASK 3: Implementar @Transactional, migraciones Flyway y use cases con transacciones optimizadas
```

---

## Próximos Pasos (Opcional)

1. **Integración de ramas**:
   - Crear PRs desde cada feature hacia HU-semana4
   - Crear PR desde HU-semana4 hacia develop

2. **Testing**:
   - Tests unitarios para Specifications
   - Tests de integración para transacciones
   - Tests de migraciones Flyway

3. **Documentación adicional**:
   - API Documentation (Swagger)
   - Diagramas de relaciones
   - Performance benchmarks

4. **Mejoras futuras**:
   - Auditoría de cambios
   - Soft deletes para categorías
   - Historial de transacciones

---

## Story Points: 10 ✅

**Complejidad**: Alta
**Esfuerzo**: ~40 horas
**Riesgo**: Bajo (patrones bien establecidos)

---

**Estado Final**: ✅ COMPLETADA
**Fecha Creación**: Diciembre 2, 2025
**Entregables**: Código funcional, 3 documentos, 3 ramas con commits
