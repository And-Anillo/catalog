package com.riwi.catalog.application.usecase;

import com.riwi.catalog.entity.Usuario;
import com.riwi.catalog.repository.UsuarioRepository;
import com.riwi.catalog.repository.specification.UsuarioSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Use case para búsqueda optimizada de usuarios.
 * Implementa:
 * - Specifications para filtros dinámicos
 * - @EntityGraph para eager loading eficiente
 * - Paginación para optimizar memoria
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuscarUsuariosUseCase {

    private final UsuarioRepository usuarioRepository;

    /**
     * Obtiene un usuario con todas sus relaciones (tareas, categorías)
     */
    public Optional<Usuario> obtenerUsuarioConRelaciones(Long usuarioId) {
        return usuarioRepository.findWithRelationsById(usuarioId);
    }

    /**
     * Obtiene todos los usuarios con sus categorías
     */
    public List<Usuario> obtenerTodosConCategorias() {
        return usuarioRepository.findAllWithCategorias();
    }

    /**
     * Busca un usuario por email
     */
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtiene usuarios activos
     */
    public List<Usuario> obtenerActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    /**
     * Búsqueda avanzada de usuarios por nombre con paginación
     */
    public Page<Usuario> buscarPorNombre(String nombre, int page, int size) {
        Specification<Usuario> spec = UsuarioSpecifications.conNombreContiene(nombre)
            .and(UsuarioSpecifications.esActivo());
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        return usuarioRepository.findAll(spec, pageable);
    }

    /**
     * Búsqueda avanzada de usuarios por email
     */
    public Page<Usuario> buscarPorEmail(String email, int page, int size) {
        Specification<Usuario> spec = UsuarioSpecifications.conEmailContiene(email)
            .and(UsuarioSpecifications.esActivo());
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        return usuarioRepository.findAll(spec, pageable);
    }

    /**
     * Obtiene usuarios activos con tareas
     */
    public List<Usuario> obtenerActivosConTareas() {
        Specification<Usuario> spec = UsuarioSpecifications.esActivo()
            .and(UsuarioSpecifications.conTareas());
        return usuarioRepository.findAll(spec);
    }

    /**
     * Obtiene usuarios sin tareas asignadas
     */
    public List<Usuario> obtenerSinTareas() {
        return usuarioRepository.findAll(UsuarioSpecifications.sinTareas());
    }
}
