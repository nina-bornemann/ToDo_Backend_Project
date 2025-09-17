package com.ninabornemann.todo_backend_project.controller;

import com.ninabornemann.todo_backend_project.dto.ToDoDto;
import com.ninabornemann.todo_backend_project.models.Todo;
import com.ninabornemann.todo_backend_project.repository.TodoRepo;
import com.ninabornemann.todo_backend_project.service.TodoService;
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

    private final TodoService service;

    public ToDoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Todo> getAllToDos() {
        return service.getAllToDos();
    }

    @PostMapping
    public ResponseEntity<Todo> addToDo(@RequestBody ToDoDto dto) {
        Todo created = service.addToDo(dto);
        return ResponseEntity.created(URI.create("/api/todo" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    public Optional<Todo> getToDoById(@PathVariable String id) {
        return service.getToDoById(id);
    }

    @PutMapping("/{id}")
    public Todo updateToDoById(@PathVariable String id, @RequestBody ToDoDto dto) {
        return service.updateToDoById(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
