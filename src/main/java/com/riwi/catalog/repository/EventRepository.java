package com.riwi.catalog.repository;

import com.riwi.catalog.model.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    // TASK 2: Validar duplicados en nombres de eventos
    // Busca por nombre e ID diferente (para el caso de UPDATE)
    Optional<EventEntity> findByNameAndIdNot(String name, Long id);

    // TASK 3: Paginación y Filtros opcionales
    Page<EventEntity> findAllByVenueCityAndCategoryAndStartDateAfter(
            String city,
            String category,
            LocalDateTime startDate,
            Pageable pageable
    );

    // Sobrecarga para búsqueda solo con paginación
    Page<EventEntity> findAll(Pageable pageable);
}
