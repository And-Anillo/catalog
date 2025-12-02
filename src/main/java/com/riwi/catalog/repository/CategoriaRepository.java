package com.riwi.catalog.repository;

import com.riwi.catalog.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {

    Optional<Categoria> findByNombre(String nombre);

    @EntityGraph(attributePaths = {"tareas", "usuarios"})
    Optional<Categoria> findWithRelationsById(Long id);

    List<Categoria> findByActivoTrue();
}
