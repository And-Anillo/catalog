package com.riwi.catalog.infrastructure.output.persistence.mapper;

import com.riwi.catalog.domain.model.User;
import com.riwi.catalog.infrastructure.output.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole());
    }
}
