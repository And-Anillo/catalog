package com.riwi.catalog.application.usecase;

import com.riwi.catalog.entity.Usuario;
import com.riwi.catalog.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case para creación y actualización de usuarios con transacciones optimizadas.
 * 
 * Implementa:
 * - @Transactional con readOnly=false para escritura
 * - Propagation.REQUIRED para transacciones anidadas
 * - Manejo de errores y rollback automático
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CrearActualizarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    /**
     * Crea un nuevo usuario.
     * 
     * Transacción: REQUIRED (usa transacción existente o crea una nueva)
     * ReadOnly: false (implícito para CREATE/UPDATE)
     * Propagation: Se propaga a transacciones padre si las hay
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Usuario crearUsuario(Usuario usuario) {
        log.info("Iniciando creación de usuario: {}", usuario.getEmail());
        
        try {
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            log.info("Usuario creado exitosamente con ID: {}", usuarioGuardado.getId());
            return usuarioGuardado;
        } catch (Exception e) {
            log.error("Error al crear usuario", e);
            throw new RuntimeException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza un usuario existente.
     * 
     * Transacción: REQUIRED
     * ReadOnly: false
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Usuario actualizarUsuario(Long usuarioId, Usuario usuarioActualizado) {
        log.info("Iniciando actualización de usuario con ID: {}", usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setDescripcion(usuarioActualizado.getDescripcion());
        usuario.setActivo(usuarioActualizado.getActivo());

        Usuario usuarioActualizado_saved = usuarioRepository.save(usuario);
        log.info("Usuario actualizado exitosamente con ID: {}", usuarioActualizado_saved.getId());
        
        return usuarioActualizado_saved;
    }

    /**
     * Desactiva un usuario (soft delete).
     * 
     * Transacción: REQUIRED
     * ReadOnly: false
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void desactivarUsuario(Long usuarioId) {
        log.info("Desactivando usuario con ID: {}", usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        
        log.info("Usuario desactivado exitosamente con ID: {}", usuarioId);
    }

    /**
     * Elimina un usuario y todas sus tareas asociadas.
     * 
     * Transacción: REQUIRES_NEW para garantizar que la eliminación se completa
     * ReadOnly: false
     * 
     * orphanRemoval=true en Usuario.tareas garantiza la eliminación de tareas huérfanas
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void eliminarUsuario(Long usuarioId) {
        log.info("Eliminando usuario con ID: {} y todas sus tareas", usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        // Las tareas se eliminarán automáticamente debido a orphanRemoval=true
        usuarioRepository.delete(usuario);
        
        log.info("Usuario y sus tareas eliminados exitosamente con ID: {}", usuarioId);
    }
}
