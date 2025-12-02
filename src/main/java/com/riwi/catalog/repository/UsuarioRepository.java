package com.riwi.catalog.repository;

import com.riwi.catalog.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByEmail(String email);

    @EntityGraph(attributePaths = {"tareas", "categorias"})
    Optional<Usuario> findWithRelationsById(Long id);

    @EntityGraph(attributePaths = {"categorias"})
    List<Usuario> findAllWithCategorias();

    List<Usuario> findByActivoTrue();
}
