package com.example.taskservice.domain.ports.out;

import com.example.taskservice.domain.model.Task;

public interface ExternalUserPort {
    boolean existsUserById(Long userId);
}
