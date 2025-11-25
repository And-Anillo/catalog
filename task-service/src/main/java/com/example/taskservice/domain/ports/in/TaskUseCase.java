package com.example.taskservice.domain.ports.in;

import com.example.taskservice.domain.model.Task;

public interface TaskUseCase {
    Task createTask(Task task);
}
