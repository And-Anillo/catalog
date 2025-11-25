package com.example.taskservice.infrastructure.input.rest;

import com.example.taskservice.domain.model.Task;
import com.example.taskservice.domain.ports.in.TaskUseCase;
import com.example.taskservice.infrastructure.input.rest.dto.TaskRequest;
import com.example.taskservice.infrastructure.input.rest.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskUseCase taskUseCase;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        Task task = new Task(null, request.getTitle(), request.getDescription(), request.getUserId(),
                request.getStatus());
        Task createdTask = taskUseCase.createTask(task);
        return new ResponseEntity<>(toResponse(createdTask), HttpStatus.CREATED);
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getUserId(),
                task.getStatus());
    }
}
