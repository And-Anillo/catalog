package com.riwi.catalog.infrastructure.output.persistence.repository;

import com.riwi.catalog.infrastructure.output.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
}
