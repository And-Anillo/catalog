package com.riwi.catalog.repository.specification;

import com.riwi.catalog.entity.Categoria;
import org.springframework.data.jpa.domain.Specification;

public class CategoriaSpecifications {

    private CategoriaSpecifications() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Especificación para filtrar categorías por nombre (búsqueda parcial)
     */
    public static Specification<Categoria> conNombreContiene(String nombre) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("nombre")),
                "%" + nombre.toLowerCase() + "%"
            );
    }

    /**
     * Especificación para filtrar categorías activas
     */
    public static Specification<Categoria> esActiva() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("activo"), true);
    }

    /**
     * Especificación para filtrar categorías inactivas
     */
    public static Specification<Categoria> esInactiva() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("activo"), false);
    }

    /**
     * Especificación para filtrar categorías con tareas
     */
    public static Specification<Categoria> conTareas() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.isNotEmpty(root.get("tareas"));
        };
    }

    /**
     * Especificación para filtrar categorías sin tareas
     */
    public static Specification<Categoria> sinTareas() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.isEmpty(root.get("tareas"));
        };
    }

    /**
     * Especificación para filtrar categorías con usuarios asociados
     */
    public static Specification<Categoria> conUsuarios() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.isNotEmpty(root.get("usuarios"));
        };
    }
}
