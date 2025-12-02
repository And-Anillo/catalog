# Optimización de Consultas - Task 2

## Estrategias Implementadas

### 1. JPQL con JOIN FETCH
- **Propósito**: Eliminar N+1 queries problema
- **Métodos**:
  - `findByUsuarioIdWithUsuario()`: Obtiene tareas con usuario asociado en una sola query
  - `findByUsuarioIdAndEstado()`: Filtra tareas por usuario y estado
  - `findByUsuarioIdAndFechaVencimiento()`: Búsqueda en rango de fechas

### 2. Specifications para Filtros Dinámicos
- **Clases**:
  - `TareaSpecifications`: Filtros para tareas (estado, usuario, categoría, prioridad, fechas, etc.)
  - `UsuarioSpecifications`: Filtros para usuarios (nombre, email, tareas)
  - `CategoriaSpecifications`: Filtros para categorías (nombre, estado, tareas)

- **Características**:
  - Composición de filtros con `Specification.where()`
  - Búsquedas case-insensitive (toLowerCase())
  - Filtros de rango (fechas)
  - Filtros de relaciones vacías/llenas

### 3. @EntityGraph para Eager Loading Controlado
- **Implementación** en repositorios:
  - `findWithRelationsById()`: Carga una entidad con sus relaciones principales
  - Evita lazy loading innecesario

### 4. Paginación
- **Uso**: En `BuscarTareasUseCase.buscarTareasConFiltros()`
- **Beneficios**:
  - Reduce carga de memoria
  - Mejora tiempo de respuesta
  - Permite ordenamiento: por prioridad DESC y fecha vencimiento ASC

### 5. batchSize en Hibernate
- **Configuración**: `spring.jpa.properties.hibernate.default_batch_fetch_size=10`
- **Propósito**: Reduce queries agrupando lazily-loaded associations
- **Ejemplo**: Si se cargan 100 usuarios con sus tareas, en lugar de 101 queries (1 + 100), hace 11 queries (1 + 10 batches)

### 6. Índices en BD
- Creados en:
  - `usuario.email`
  - `usuario.nombre`
  - `tarea.usuario_id`
  - `tarea.estado`
  - `tarea.fecha_vencimiento`
  - `tarea.categoria_id`
  - `categoria.nombre`

## Use Cases Implementados

### BuscarTareasUseCase
Métodos para búsqueda optimizada de tareas:
- `obtenerTareaConRelaciones()`: @EntityGraph
- `obtenerTareasDelUsuario()`: JOIN FETCH JPQL
- `buscarTareasDelUsuarioPorEstado()`: JPQL directo
- `buscarTareasConFiltros()`: Specifications con paginación
- `obtenerTareasVencidas()`: Compositions de Specifications
- `obtenerTareasPorVencer()`: Rango de fechas
- `obtenerTareasSinCategoria()`: Filtros complejos

### BuscarUsuariosUseCase
Métodos para búsqueda optimizada de usuarios:
- `obtenerUsuarioConRelaciones()`: @EntityGraph
- `buscarPorNombre()`: Specifications + paginación
- `obtenerActivosConTareas()`: Compositions avanzadas
- `obtenerSinTareas()`: Filtros de colecciones vacías

## Métricas de Mejora

### Antes
- N+1 queries problem: Query principal + N queries por cada relación
- Sin paginación: Carga toda la BD en memoria
- Sin índices: Full table scans

### Después
- Single query con @EntityGraph
- Batch loading con batchSize=10
- Paginación eficiente
- Índices optimizados
- Composición de Specifications reutilizable

## Configuración en application.properties

```properties
# Batch Loading
spring.jpa.properties.hibernate.default_batch_fetch_size=10

# Logging SQL para verificar queries
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

## Pruebas de Rendimiento

Para verificar mejoras, monitorear:
1. **SQL Logs**: Ver cantidad y estructura de queries
2. **Time**: Tiempo total de ejecución
3. **Memory**: Heap usage antes/después de cambios
