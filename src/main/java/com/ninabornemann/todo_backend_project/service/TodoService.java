package com.ninabornemann.todo_backend_project.service;

import com.ninabornemann.todo_backend_project.dto.ToDoDto;
import com.ninabornemann.todo_backend_project.models.Todo;
import com.ninabornemann.todo_backend_project.repository.TodoRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepo repo;

    public TodoService(TodoRepo repo) {
        this.repo = repo;
    }

    public List<Todo> getAllToDos() {
        return repo.findAll();
    }

    public ResponseEntity<Todo> addToDo(ToDoDto dto) {
        Todo newToDo = new Todo(UUID.randomUUID().toString(), dto.description(),dto.status());
        repo.save(newToDo);
        return ResponseEntity.created(URI.create("/api/todo" + newToDo.id())).body(newToDo);
    }

    public Optional<Todo> getToDoById(String id) {
        return Optional.ofNullable(repo.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    public Todo updateToDoById(String id, ToDoDto dto) {
        Todo existing = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No To-Do was found under this id."));
        Todo updated  = existing.withDescription(dto.description());
        updated = updated.withStatus(dto.status());
        repo.delete(existing);
        repo.save(updated);
        return updated;
    }

    public ResponseEntity<Todo> deleteById(String id) {
        Todo existing = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No To-Do was found under this id."));
        repo.delete(existing);
        return ResponseEntity.noContent().build();
    }

}
