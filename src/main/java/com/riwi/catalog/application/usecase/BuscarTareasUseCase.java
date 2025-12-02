package com.riwi.catalog.application.usecase;

import com.riwi.catalog.entity.Tarea;
import com.riwi.catalog.entity.Tarea.EstadoTarea;
import com.riwi.catalog.application.dto.TareaFilterDTO;
import com.riwi.catalog.repository.TareaRepository;
import com.riwi.catalog.repository.specification.TareaSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Use case para búsqueda y filtrado optimizado de tareas.
 * Implementa:
 * - JPQL con join fetch para evitar N+1 queries
 * - Specifications para filtros dinámicos
 * - Paginación para optimizar memoria
 * - @EntityGraph para eager loading controlado
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuscarTareasUseCase {

    private final TareaRepository tareaRepository;

    /**
     * Busca una tarea por ID con sus relaciones (usuario, categoría)
     */
    public Optional<Tarea> obtenerTareaConRelaciones(Long tareaId) {
        return tareaRepository.findWithRelationsById(tareaId);
    }

    /**
     * Obtiene todas las tareas de un usuario con relaciones
     */
    public List<Tarea> obtenerTareasDelUsuario(Long usuarioId) {
        return tareaRepository.findByUsuarioIdWithUsuario(usuarioId);
    }

    /**
     * Busca tareas filtrando por usuario y estado
     */
    public List<Tarea> buscarTareasDelUsuarioPorEstado(Long usuarioId, EstadoTarea estado) {
        return tareaRepository.findByUsuarioIdAndEstado(usuarioId, estado);
    }

    /**
     * Busca tareas en un rango de fecha de vencimiento
     */
    public List<Tarea> buscarTareasEnRangoFecha(Long usuarioId, LocalDateTime desde, LocalDateTime hasta) {
        return tareaRepository.findByUsuarioIdAndFechaVencimiento(usuarioId, desde, hasta);
    }

    /**
     * Busca tareas por estado ordenadas por prioridad
     */
    public List<Tarea> buscarTareasActivasPorPrioridad(EstadoTarea estado) {
        return tareaRepository.findByEstadoOrderByPrioridad(estado);
    }

    /**
     * Búsqueda avanzada con múltiples filtros usando Specifications
     * Evita N+1 queries mediante join fetch implícito
     */
    public Page<Tarea> buscarTareasConFiltros(TareaFilterDTO filtro, int page, int size) {
        Specification<Tarea> spec = Specification.where(null);

        if (filtro.getUsuarioId() != null) {
            spec = spec.and(TareaSpecifications.conUsuarioId(filtro.getUsuarioId()));
        }

        if (filtro.getEstado() != null) {
            spec = spec.and(TareaSpecifications.conEstado(EstadoTarea.valueOf(filtro.getEstado())));
        }

        if (filtro.getCategoriaId() != null) {
            spec = spec.and(TareaSpecifications.conCategoriaId(filtro.getCategoriaId()));
        }

        if (filtro.getPrioridad() != null) {
            spec = spec.and(TareaSpecifications.conPrioridad(Tarea.Prioridad.valueOf(filtro.getPrioridad())));
        }

        if (filtro.getTitulo() != null && !filtro.getTitulo().isBlank()) {
            spec = spec.and(TareaSpecifications.conTituloContiene(filtro.getTitulo()));
        }

        if (filtro.getDescripcion() != null && !filtro.getDescripcion().isBlank()) {
            spec = spec.and(TareaSpecifications.conDescripcionContiene(filtro.getDescripcion()));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("prioridad").descending().and(Sort.by("fechaVencimiento").ascending()));
        return tareaRepository.findAll(spec, pageable);
    }

    /**
     * Busca tareas vencidas para un usuario
     */
    public List<Tarea> obtenerTareasVencidas(Long usuarioId) {
        Specification<Tarea> spec = TareaSpecifications.conUsuarioId(usuarioId)
            .and(TareaSpecifications.conFechaVencimientoPasada())
            .and(TareaSpecifications.conEstado(EstadoTarea.PENDIENTE).or(TareaSpecifications.conEstado(EstadoTarea.EN_PROGRESO)));
        return tareaRepository.findAll(spec);
    }

    /**
     * Busca tareas proximas a vencer
     */
    public List<Tarea> obtenerTareasPorVencer(Long usuarioId, int diasAntelacion) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fecha = ahora.plusDays(diasAntelacion);
        Specification<Tarea> spec = TareaSpecifications.conUsuarioId(usuarioId)
            .and(TareaSpecifications.conFechaVencimientoBetween(ahora, fecha))
            .and(TareaSpecifications.conEstado(EstadoTarea.PENDIENTE).or(TareaSpecifications.conEstado(EstadoTarea.EN_PROGRESO)));
        return tareaRepository.findAll(spec);
    }

    /**
     * Busca tareas por categoría
     */
    public List<Tarea> obtenerTareasPorCategoria(Long categoriaId) {
        return tareaRepository.findByCategoriaId(categoriaId);
    }

    /**
     * Busca tareas sin categoría asignada
     */
    public List<Tarea> obtenerTareasSinCategoria() {
        return tareaRepository.findAll(TareaSpecifications.sinCategoria());
    }

    /**
     * Obtiene todas las tareas completadas
     */
    public List<Tarea> obtenerTareasCompletadas() {
        return tareaRepository.findByEstado(EstadoTarea.COMPLETADA);
    }
}
