package com.riwi.catalog.domain.ports.in;

import com.riwi.catalog.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserUseCase {
    User createUser(User user);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, User user);

    boolean deleteUser(Long id);
}
