package com.ninabornemann.todo_backend_project.controller;

import com.ninabornemann.todo_backend_project.dto.ToDoDto;
import com.ninabornemann.todo_backend_project.models.Todo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {

    private final List<Todo> allToDos = new ArrayList<>();

    @GetMapping
    public List<Todo> getAllToDos() {
        return allToDos;
    }


    @PostMapping
    public ResponseEntity<Todo> addToDo(@RequestBody ToDoDto dto) {
        Todo newToDo = new Todo(UUID.randomUUID().toString(), dto.description(),dto.status());
        allToDos.add(newToDo);
        return ResponseEntity.created(URI.create("/api/todo" + newToDo.id())).body(newToDo);
    }


    @GetMapping("/{id}")
    public Optional<Todo> getToDoById(@PathVariable String id) {
        return allToDos.stream()
                .filter(todo -> todo.id().equals(id))
                .findFirst();
    }

    @PutMapping("/{id}")
    public Todo updateToDoById(@PathVariable String id, @RequestBody ToDoDto dto) {
        Todo existing = allToDos.stream()
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No To-Do was found under this id."));
        Todo updated  = existing.withDescription(dto.description());
        updated = updated.withStatus(dto.status());
        allToDos.remove(existing);
        allToDos.add(updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo> deleteById(@PathVariable String id) {
        Todo existing = allToDos.stream()
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No To-Do was found under this id."));
        allToDos.remove(existing);
        return ResponseEntity.noContent().build();
    }

}
