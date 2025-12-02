# Transaccionalidad y Migraciones con Flyway - Task 3

## Configuración de @Transactional

### Niveles de Propagación Implementados

#### 1. PROPAGATION.REQUIRED (Por defecto)
- **Uso**: Operaciones CRUD normales
- **Comportamiento**: 
  - Si existe transacción padre, la usa
  - Si no existe, crea una nueva
- **Métodos**:
  - `crearUsuario()`
  - `actualizarUsuario()`
  - `crearTarea()`
  - `actualizarTarea()`
  - `cambiarEstadoTarea()`

#### 2. PROPAGATION.REQUIRES_NEW
- **Uso**: Operaciones que deben ser independientes
- **Comportamiento**:
  - Siempre crea una NUEVA transacción
  - Suspende la transacción padre si existe
  - Rollback independiente
- **Métodos**:
  - `eliminarUsuario()`: Garantiza que usuario + tareas se eliminan juntos
  - `reasignarTarea()`: Operación atómica de reasignación
  - `eliminarTarea()`: Eliminación independiente
  - `eliminarTodasLasTareasDelUsuario()`: Operación en lote

### Diferencia entre Read-Only y Write Transactions

#### ReadOnly = true (Lectura)
```java
@Transactional(readOnly = true)
public Optional<Usuario> obtenerUsuarioConRelaciones(Long usuarioId) {
    return usuarioRepository.findWithRelationsById(usuarioId);
}
```
- **Optimizaciones**:
  - Hibernate no registra cambios
  - Mejor rendimiento
  - No requiere flush
  - Ideal para queries complejas

#### ReadOnly = false (Escritura - implícito)
```java
@Transactional(propagation = Propagation.REQUIRED)
public Usuario crearUsuario(Usuario usuario) {
    return usuarioRepository.save(usuario);
}
```
- **Comportamiento**:
  - Registra cambios en memoria
  - Flush antes de commit
  - Puede disparar cascadas
  - Permite actualizaciones

### Mapeo de Transacciones a Use Cases

| Use Case | Transacción | Propagación | ReadOnly | Propósito |
|----------|-------------|-------------|----------|-----------|
| BuscarTareasUseCase | @Transactional | REQUIRED (implícito) | true | Búsquedas seguras |
| BuscarUsuariosUseCase | @Transactional | REQUIRED (implícito) | true | Búsquedas seguras |
| CrearActualizarUsuario | @Transactional | REQUIRED | false | Operaciones CRUD |
| CrearActualizarTarea | @Transactional | REQUIRED/REQUIRES_NEW | false | Operaciones CRUD |

## Flyway - Migraciones Versionadas

### Estructura de Migraciones

```
src/main/resources/db/migration/
├── V1__init.sql           # Schema inicial
├── V2__relaciones.sql     # Relaciones y constraints avanzados
└── V3__test_data.sql      # Datos de prueba
```

### V1__init.sql - Esquema Inicial

**Crea**:
- Tabla `usuario` (PK: id)
- Tabla `categoria` (PK: id)
- Tabla `tarea` (PK: id, FK: usuario_id, categoria_id)
- Tabla `usuario_categoria` (ManyToMany junction table)

**Índices**:
- usuario.email (UNIQUE)
- usuario.nombre
- tarea.usuario_id
- tarea.estado
- tarea.fecha_vencimiento
- categoria.nombre

**Constraints**:
- Foreign Keys con ON DELETE CASCADE
- Valores por defecto (estado = PENDIENTE, prioridad = MEDIA, activo = true)

### V2__relaciones.sql - Constraints Avanzados

**Agrega**:
- Composite indexes para query patterns:
  - (usuario_id, estado)
  - (usuario_id, fecha_vencimiento)
  - (estado, prioridad)

- CHECK constraints:
  - estado IN ('PENDIENTE', 'EN_PROGRESO', 'COMPLETADA', 'CANCELADA')
  - prioridad IN ('BAJA', 'MEDIA', 'ALTA', 'URGENTE')

- Documentación de tablas

### V3__test_data.sql - Datos de Prueba

**Inserta**:
- 4 usuarios (1 inactivo)
- 5 categorías
- 13 tareas con diferentes estados
- Relaciones usuario-categoría

**Propósito**:
- Pruebas de búsqueda y filtros
- Validación de relaciones
- Verificación de rendimiento de queries

## Configuración en application.properties

```properties
# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baselineOnMigrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.outOfOrder=false

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
```

### Explicación de Configuraciones

| Propiedad | Valor | Razón |
|-----------|-------|-------|
| `enabled` | true | Activa migraciones automáticas |
| `baselineOnMigrate` | true | Permite baseline para DBs existentes |
| `locations` | classpath:db/migration | Ubicación de scripts SQL |
| `outOfOrder` | false | Las migraciones deben ser en orden |
| `ddl-auto` | validate | Valida que esquema coincida con entidades |

## Ciclo de Vida de Transacciones en JPA

### 1. PERSIST
```
Usuario usuario = new Usuario();
usuarioRepository.save(usuario);
// Estado: MANAGED (en el contexto de persistencia)
```

### 2. MERGE
```
usuario.setNombre("Nuevo nombre");
usuarioRepository.save(usuario);
// El objeto se sincroniza con la BD
```

### 3. DETACH
```
// Fin de @Transactional sin flush() explícito
// El objeto sale del contexto de persistencia
```

### 4. REMOVE
```
usuarioRepository.delete(usuario);
// Marca para eliminación, se ejecuta en flush/commit
```

## Cascadas Configuradas

### En Usuario.tareas
```java
@OneToMany(
    mappedBy = "usuario",
    cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    orphanRemoval = true,
    fetch = FetchType.LAZY
)
```
- **PERSIST**: Nuevas tareas se persisten automáticamente
- **MERGE**: Cambios en tareas se sincronizan
- **orphanRemoval = true**: Tareas sin usuario se eliminan
- **fetch = LAZY**: Las tareas se cargan bajo demanda

### En Usuario.categorias
```java
@ManyToMany(
    cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    fetch = FetchType.LAZY
)
```
- No se usa REMOVE ni orphanRemoval
- Un usuario inactivo no elimina categorías (relación lógica)

## Manejo de Errores y Rollback

### Rollback Automático

```java
@Transactional
public void eliminarUsuario(Long usuarioId) {
    usuarioRepository.deleteById(usuarioId);
    if (usuarioId == 999) {
        throw new RuntimeException("Error simulado");
        // Rollback automático - nada se guarda
    }
}
```

### Excepciones No-Rollback

Por defecto, Spring rollback en excepciones de tipo `RuntimeException`.

Para excepciones de tipo `checked`, usar:
```java
@Transactional(rollbackFor = Exception.class)
```

## Validación en Entornos Limpios

### Con H2 (In-Memory)
```properties
spring.datasource.url=jdbc:h2:mem:catalog;MODE=MySQL
```
- Cada ejecución comienza limpia
- Migraciones se ejecutan automáticamente
- Ideal para testing

### Con MySQL Local
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/catalog
spring.datasource.username=root
spring.datasource.password=password
```
- Flyway mantiene tabla `flyway_schema_history`
- Puede ser ejecutado múltiples veces de forma segura

## Pruebas de Transaccionalidad

### Verificar Migraciones
```bash
# Ver estado de migraciones en la BD
SELECT * FROM flyway_schema_history;
```

### Probar Rollback
```java
@Test
public void testRollbackOnError() {
    assertThrows(RuntimeException.class, () -> {
        usuarioService.crearUsuarioConError();
    });
    // Verificar que usuario NO se guardó
    assertEquals(0, usuarioRepository.count());
}
```

### Monitorear Transacciones
```properties
logging.level.org.springframework.transaction=DEBUG
logging.level.org.hibernate.engine.transaction.internal=DEBUG
```
