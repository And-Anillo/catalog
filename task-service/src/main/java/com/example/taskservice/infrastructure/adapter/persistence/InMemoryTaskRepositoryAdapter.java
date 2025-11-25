package com.example.taskservice.infrastructure.adapter.persistence;

import com.example.taskservice.domain.model.Task;
import com.example.taskservice.domain.ports.out.TaskRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryTaskRepositoryAdapter implements TaskRepositoryPort {

    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator.getAndIncrement());
        }
        tasks.put(task.getId(), task);
        return task;
    }
}
