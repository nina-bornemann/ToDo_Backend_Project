package com.ninabornemann.todo_backend_project.controller;

import com.ninabornemann.todo_backend_project.dto.ToDoDto;
import com.ninabornemann.todo_backend_project.models.Todo;
import com.ninabornemann.todo_backend_project.repository.TodoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {

    private final TodoRepo repo;

    public ToDoController(TodoRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Todo> getAllToDos() {
        return repo.findAll();
    }


    @PostMapping
    public ResponseEntity<Todo> addToDo(@RequestBody ToDoDto dto) {
        Todo newToDo = new Todo(UUID.randomUUID().toString(), dto.description(),dto.status());
        repo.save(newToDo);
        return ResponseEntity.created(URI.create("/api/todo" + newToDo.id())).body(newToDo);
    }


    @GetMapping("/{id}")
    public Optional<Todo> getToDoById(@PathVariable String id) {
        return Optional.ofNullable(repo.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    @PutMapping("/{id}")
    public Todo updateToDoById(@PathVariable String id, @RequestBody ToDoDto dto) {
        Todo existing = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No To-Do was found under this id."));
        Todo updated  = existing.withDescription(dto.description());
        updated = updated.withStatus(dto.status());
        repo.delete(existing);
        repo.save(updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo> deleteById(@PathVariable String id) {
        Todo existing = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No To-Do was found under this id."));
        repo.delete(existing);
        return ResponseEntity.noContent().build();
    }

}
