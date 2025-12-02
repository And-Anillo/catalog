package com.riwi.catalog.repository;

import com.riwi.catalog.entity.Tarea;
import com.riwi.catalog.entity.Tarea.EstadoTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    @EntityGraph(attributePaths = {"usuario", "categoria"})
    Optional<Tarea> findWithRelationsById(Long id);

    @Query("SELECT t FROM Tarea t JOIN FETCH t.usuario u WHERE u.id = :usuarioId")
    List<Tarea> findByUsuarioIdWithUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT t FROM Tarea t WHERE t.usuario.id = :usuarioId AND t.estado = :estado")
    List<Tarea> findByUsuarioIdAndEstado(@Param("usuarioId") Long usuarioId, @Param("estado") EstadoTarea estado);

    @Query("SELECT t FROM Tarea t WHERE t.usuario.id = :usuarioId AND t.fechaVencimiento BETWEEN :fechaInicio AND :fechaFin")
    List<Tarea> findByUsuarioIdAndFechaVencimiento(
        @Param("usuarioId") Long usuarioId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT t FROM Tarea t WHERE t.estado = :estado ORDER BY t.prioridad DESC")
    List<Tarea> findByEstadoOrderByPrioridad(@Param("estado") EstadoTarea estado);

    List<Tarea> findByEstado(EstadoTarea estado);

    List<Tarea> findByCategoriaId(Long categoriaId);
}
