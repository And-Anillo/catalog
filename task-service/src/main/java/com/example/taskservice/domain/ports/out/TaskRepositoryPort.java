package com.example.taskservice.domain.ports.out;

import com.example.taskservice.domain.model.Task;

public interface TaskRepositoryPort {
    Task save(Task task);
}
