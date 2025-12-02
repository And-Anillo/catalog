package com.riwi.catalog.application.usecase;

import com.riwi.catalog.entity.Tarea;
import com.riwi.catalog.entity.Usuario;
import com.riwi.catalog.repository.TareaRepository;
import com.riwi.catalog.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case para creación, actualización y gestión de tareas con transacciones optimizadas.
 * 
 * Implementa:
 * - @Transactional con diferentes niveles de propagación
 * - Transacciones de lectura (readOnly=true) para búsquedas
 * - Transacciones de escritura para CREATE/UPDATE/DELETE
 * - Manejo de relaciones entre Usuario y Tarea
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CrearActualizarTareaUseCase {

    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Crea una nueva tarea para un usuario.
     * 
     * Transacción: REQUIRED
     * ReadOnly: false
     * 
     * El usuario debe existir antes de crear la tarea
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Tarea crearTarea(Tarea tarea, Long usuarioId) {
        log.info("Iniciando creación de tarea para usuario con ID: {}", usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        tarea.setUsuario(usuario);
        Tarea tareaGuardada = tareaRepository.save(tarea);
        
        log.info("Tarea creada exitosamente con ID: {} para usuario: {}", tareaGuardada.getId(), usuarioId);
        return tareaGuardada;
    }

    /**
     * Actualiza una tarea existente.
     * 
     * Transacción: REQUIRED
     * ReadOnly: false
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Tarea actualizarTarea(Long tareaId, Tarea tareaActualizada) {
        log.info("Iniciando actualización de tarea con ID: {}", tareaId);
        
        Tarea tarea = tareaRepository.findById(tareaId)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + tareaId));

        tarea.setTitulo(tareaActualizada.getTitulo());
        tarea.setDescripcion(tareaActualizada.getDescripcion());
        tarea.setEstado(tareaActualizada.getEstado());
        tarea.setFechaVencimiento(tareaActualizada.getFechaVencimiento());
        tarea.setPrioridad(tareaActualizada.getPrioridad());
        tarea.setCategoria(tareaActualizada.getCategoria());

        Tarea tareaActualizado_saved = tareaRepository.save(tarea);
        log.info("Tarea actualizada exitosamente con ID: {}", tareaActualizado_saved.getId());
        
        return tareaActualizado_saved;
    }

    /**
     * Cambia el estado de una tarea.
     * 
     * Transacción: REQUIRED
     * ReadOnly: false
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Tarea cambiarEstadoTarea(Long tareaId, Tarea.EstadoTarea nuevoEstado) {
        log.info("Cambiando estado de tarea con ID: {} a: {}", tareaId, nuevoEstado);
        
        Tarea tarea = tareaRepository.findById(tareaId)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + tareaId));

        tarea.setEstado(nuevoEstado);
        Tarea tareaActualizada = tareaRepository.save(tarea);
        
        log.info("Estado de tarea cambiado exitosamente a: {}", nuevoEstado);
        return tareaActualizada;
    }

    /**
     * Asigna una tarea a un usuario diferente.
     * 
     * Transacción: REQUIRES_NEW para asegurar que la reasignación se completa independientemente
     * ReadOnly: false
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Tarea reasignarTarea(Long tareaId, Long nuevoUsuarioId) {
        log.info("Reasignando tarea con ID: {} a usuario con ID: {}", tareaId, nuevoUsuarioId);
        
        Tarea tarea = tareaRepository.findById(tareaId)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + tareaId));

        Usuario nuevoUsuario = usuarioRepository.findById(nuevoUsuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + nuevoUsuarioId));

        tarea.setUsuario(nuevoUsuario);
        Tarea tareaReasignada = tareaRepository.save(tarea);
        
        log.info("Tarea reasignada exitosamente a usuario con ID: {}", nuevoUsuarioId);
        return tareaReasignada;
    }

    /**
     * Elimina una tarea.
     * 
     * Transacción: REQUIRES_NEW para garantizar eliminación independiente
     * ReadOnly: false
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void eliminarTarea(Long tareaId) {
        log.info("Eliminando tarea con ID: {}", tareaId);
        
        Tarea tarea = tareaRepository.findById(tareaId)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + tareaId));

        tareaRepository.delete(tarea);
        
        log.info("Tarea eliminada exitosamente con ID: {}", tareaId);
    }

    /**
     * Elimina todas las tareas de un usuario.
     * 
     * Transacción: REQUIRES_NEW para garantizar operación atómica
     * ReadOnly: false
     * 
     * PRECAUCIÓN: Esta operación elimina TODAS las tareas del usuario
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void eliminarTodasLasTareasDelUsuario(Long usuarioId) {
        log.warn("Eliminando TODAS las tareas del usuario con ID: {}", usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        usuario.getTareas().clear();
        usuarioRepository.save(usuario);
        
        log.warn("Todas las tareas del usuario con ID: {} han sido eliminadas", usuarioId);
    }
}
