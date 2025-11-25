package com.riwi.catalog.application.service;

import com.riwi.catalog.domain.model.User;
import com.riwi.catalog.domain.ports.in.UserUseCase;
import com.riwi.catalog.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public User createUser(User user) {
        return userRepositoryPort.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepositoryPort.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepositoryPort.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        return userRepositoryPort.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setRole(user.getRole());
                    return userRepositoryPort.save(existingUser);
                })
                .orElse(null); // Or throw exception
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepositoryPort.existsById(id)) {
            userRepositoryPort.deleteById(id);
            return true;
        }
        return false;
    }
}
