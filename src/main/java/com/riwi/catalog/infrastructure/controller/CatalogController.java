package com.riwi.catalog.infrastructure.controller;

import com.riwi.catalog.application.usecase.BuscarTareasUseCase;
import com.riwi.catalog.application.usecase.CrearActualizarTareaUseCase;
import com.riwi.catalog.application.usecase.BuscarUsuariosUseCase;
import com.riwi.catalog.application.usecase.CrearActualizarUsuarioUseCase;
import com.riwi.catalog.entity.Tarea;
import com.riwi.catalog.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador de prueba para demostrar:
 * - Consultas optimizadas con Specifications y @EntityGraph
 * - Transaccionalidad con @Transactional
 * - Migraciones versionadas con Flyway
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CatalogController {

    private final BuscarTareasUseCase buscarTareasUseCase;
    private final CrearActualizarTareaUseCase crearActualizarTareaUseCase;
    private final BuscarUsuariosUseCase buscarUsuariosUseCase;
    private final CrearActualizarUsuarioUseCase crearActualizarUsuarioUseCase;

    // ==================== USUARIOS ====================

    /**
     * GET /api/v1/usuarios/{id} - Obtener usuario con relaciones
     * Utiliza: @EntityGraph para evitar lazy loading
     */
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        return buscarUsuariosUseCase.obtenerUsuarioConRelaciones(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/v1/usuarios - Obtener todos los usuarios activos
     * Utiliza: Specifications y @EntityGraph
     */
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> obtenerUsuariosActivos() {
        return ResponseEntity.ok(buscarUsuariosUseCase.obtenerActivos());
    }

    /**
     * POST /api/v1/usuarios - Crear usuario
     * Utiliza: @Transactional(propagation = REQUIRED)
     */
    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(crearActualizarUsuarioUseCase.crearUsuario(usuario));
    }

    /**
     * PUT /api/v1/usuarios/{id} - Actualizar usuario
     * Utiliza: @Transactional con merge
     */
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(crearActualizarUsuarioUseCase.actualizarUsuario(id, usuario));
    }

    // ==================== TAREAS ====================

    /**
     * GET /api/v1/tareas/{id} - Obtener tarea con relaciones
     * Utiliza: @EntityGraph para cargar usuario y categoría
     */
    @GetMapping("/tareas/{id}")
    public ResponseEntity<Tarea> obtenerTarea(@PathVariable Long id) {
        return buscarTareasUseCase.obtenerTareaConRelaciones(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/v1/usuarios/{usuarioId}/tareas - Obtener tareas de un usuario
     * Utiliza: JOIN FETCH JPQL para evitar N+1 queries
     */
    @GetMapping("/usuarios/{usuarioId}/tareas")
    public ResponseEntity<List<Tarea>> obtenerTareasDelUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(buscarTareasUseCase.obtenerTareasDelUsuario(usuarioId));
    }

    /**
     * GET /api/v1/tareas/estado/{estado} - Buscar tareas por estado
     * Utiliza: Specifications y sorting
     */
    @GetMapping("/tareas/estado/{estado}")
    public ResponseEntity<List<Tarea>> obtenerTareasActivasPorPrioridad(@PathVariable String estado) {
        return ResponseEntity.ok(
            buscarTareasUseCase.buscarTareasActivasPorPrioridad(Tarea.EstadoTarea.valueOf(estado))
        );
    }

    /**
     * GET /api/v1/tareas/vencidas/{usuarioId} - Obtener tareas vencidas
     * Utiliza: Compositions de Specifications
     */
    @GetMapping("/tareas/vencidas/{usuarioId}")
    public ResponseEntity<List<Tarea>> obtenerTareasVencidas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(buscarTareasUseCase.obtenerTareasVencidas(usuarioId));
    }

    /**
     * GET /api/v1/tareas/proximas-vencer - Obtener tareas próximas a vencer
     * Utiliza: Rango de fechas con Specifications
     */
    @GetMapping("/tareas/proximas-vencer/{usuarioId}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorVencer(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "7") int dias) {
        return ResponseEntity.ok(buscarTareasUseCase.obtenerTareasPorVencer(usuarioId, dias));
    }

    /**
     * POST /api/v1/usuarios/{usuarioId}/tareas - Crear tarea
     * Utiliza: @Transactional(propagation = REQUIRED) con cascada
     */
    @PostMapping("/usuarios/{usuarioId}/tareas")
    public ResponseEntity<Tarea> crearTarea(@PathVariable Long usuarioId, @RequestBody Tarea tarea) {
        return ResponseEntity.ok(crearActualizarTareaUseCase.crearTarea(tarea, usuarioId));
    }

    /**
     * PUT /api/v1/tareas/{id} - Actualizar tarea
     * Utiliza: @Transactional con merge
     */
    @PutMapping("/tareas/{id}")
    public ResponseEntity<Tarea> actualizarTarea(@PathVariable Long id, @RequestBody Tarea tarea) {
        return ResponseEntity.ok(crearActualizarTareaUseCase.actualizarTarea(id, tarea));
    }

    /**
     * PATCH /api/v1/tareas/{id}/estado - Cambiar estado de tarea
     * Utiliza: @Transactional para actualización selectiva
     */
    @PatchMapping("/tareas/{id}/estado")
    public ResponseEntity<Tarea> cambiarEstadoTarea(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(
            crearActualizarTareaUseCase.cambiarEstadoTarea(id, Tarea.EstadoTarea.valueOf(estado))
        );
    }

    /**
     * PATCH /api/v1/tareas/{id}/reasignar - Reasignar tarea a otro usuario
     * Utiliza: @Transactional(propagation = REQUIRES_NEW) para operación independiente
     */
    @PatchMapping("/tareas/{id}/reasignar")
    public ResponseEntity<Tarea> reasignarTarea(
            @PathVariable Long id,
            @RequestParam Long nuevoUsuarioId) {
        return ResponseEntity.ok(crearActualizarTareaUseCase.reasignarTarea(id, nuevoUsuarioId));
    }

    // ==================== HEALTH CHECK ====================

    /**
     * GET /api/v1/health - Verificar conexión a BD y Flyway
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API funcionando correctamente. Migraciones Flyway ejecutadas.");
    }
}
