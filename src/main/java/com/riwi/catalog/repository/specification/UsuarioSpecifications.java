package com.riwi.catalog.repository.specification;

import com.riwi.catalog.entity.Usuario;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecifications {

    private UsuarioSpecifications() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Especificación para filtrar usuarios por nombre (búsqueda parcial)
     */
    public static Specification<Usuario> conNombreContiene(String nombre) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("nombre")),
                "%" + nombre.toLowerCase() + "%"
            );
    }

    /**
     * Especificación para filtrar usuarios por email (búsqueda parcial)
     */
    public static Specification<Usuario> conEmailContiene(String email) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("email")),
                "%" + email.toLowerCase() + "%"
            );
    }

    /**
     * Especificación para filtrar usuarios activos
     */
    public static Specification<Usuario> esActivo() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("activo"), true);
    }

    /**
     * Especificación para filtrar usuarios inactivos
     */
    public static Specification<Usuario> esInactivo() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("activo"), false);
    }

    /**
     * Especificación para filtrar usuarios con tareas asignadas
     */
    public static Specification<Usuario> conTareas() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.isNotEmpty(root.get("tareas"));
        };
    }

    /**
     * Especificación para filtrar usuarios sin tareas asignadas
     */
    public static Specification<Usuario> sinTareas() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.isEmpty(root.get("tareas"));
        };
    }
}
