package com.riwi.catalog.domain.ports.out;

import com.riwi.catalog.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}
