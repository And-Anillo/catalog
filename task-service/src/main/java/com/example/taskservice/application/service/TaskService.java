package com.example.taskservice.application.service;

import com.example.taskservice.domain.model.Task;
import com.example.taskservice.domain.ports.in.TaskUseCase;
import com.example.taskservice.domain.ports.out.ExternalUserPort;
import com.example.taskservice.domain.ports.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService implements TaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final ExternalUserPort externalUserPort;

    @Override
    public Task createTask(Task task) {
        if (!externalUserPort.existsUserById(task.getUserId())) {
            throw new IllegalArgumentException("User not found");
        }
        return taskRepositoryPort.save(task);
    }
}
