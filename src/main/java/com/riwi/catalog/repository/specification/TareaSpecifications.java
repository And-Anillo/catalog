package com.riwi.catalog.repository.specification;

import com.riwi.catalog.entity.Tarea;
import com.riwi.catalog.entity.Tarea.EstadoTarea;
import com.riwi.catalog.entity.Tarea.Prioridad;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class TareaSpecifications {

    private TareaSpecifications() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Especificación para filtrar tareas por usuario ID
     */
    public static Specification<Tarea> conUsuarioId(Long usuarioId) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("usuario").get("id"), usuarioId);
    }

    /**
     * Especificación para filtrar tareas por estado
     */
    public static Specification<Tarea> conEstado(EstadoTarea estado) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("estado"), estado);
    }

    /**
     * Especificación para filtrar tareas por categoría ID
     */
    public static Specification<Tarea> conCategoriaId(Long categoriaId) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("categoria").get("id"), categoriaId);
    }

    /**
     * Especificación para filtrar tareas por prioridad
     */
    public static Specification<Tarea> conPrioridad(Prioridad prioridad) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("prioridad"), prioridad);
    }

    /**
     * Especificación para filtrar tareas por rango de fecha de vencimiento
     */
    public static Specification<Tarea> conFechaVencimientoBetween(LocalDateTime desde, LocalDateTime hasta) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.between(root.get("fechaVencimiento"), desde, hasta);
    }

    /**
     * Especificación para filtrar tareas por fecha de vencimiento futura
     */
    public static Specification<Tarea> conFechaVencimientoFutura() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThan(root.get("fechaVencimiento"), LocalDateTime.now());
    }

    /**
     * Especificación para filtrar tareas vencidas
     */
    public static Specification<Tarea> conFechaVencimientoPasada() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.lessThan(root.get("fechaVencimiento"), LocalDateTime.now());
    }

    /**
     * Especificación para filtrar tareas por título (búsqueda parcial)
     */
    public static Specification<Tarea> conTituloContiene(String titulo) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("titulo")),
                "%" + titulo.toLowerCase() + "%"
            );
    }

    /**
     * Especificación para filtrar tareas por descripción (búsqueda parcial)
     */
    public static Specification<Tarea> conDescripcionContiene(String descripcion) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("descripcion")),
                "%" + descripcion.toLowerCase() + "%"
            );
    }

    /**
     * Especificación para filtrar tareas sin categoría asignada
     */
    public static Specification<Tarea> sinCategoria() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.isNull(root.get("categoria"));
    }

    /**
     * Especificación para filtrar tareas con categoría asignada
     */
    public static Specification<Tarea> conCategoria() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.isNotNull(root.get("categoria"));
    }
}
