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
    private final IdService idService;
    private final ChatGPTService chatGPTService;

    public TodoService(TodoRepo repo, IdService idService, ChatGPTService chatGPTService) {
        this.repo = repo;
        this.idService = idService;
        this.chatGPTService = chatGPTService;
    }

    public List<Todo> getAllToDos() {
        return repo.findAll();
    }

    public Todo addToDo(ToDoDto dto) {
        String checkedDescription = chatGPTService.checkSpelling(dto.description());
        Todo newToDo = new Todo(idService.randomId(), checkedDescription,dto.status());
        return repo.save(newToDo);
    }

    public Optional<Todo> getToDoById(String id) {
        return Optional.ofNullable(repo.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    public Todo updateToDoById(String id, ToDoDto dto) {
        Todo existing = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No To-Do was found under this id."));
        String checkedDescription = chatGPTService.checkSpelling(dto.description());
        Todo updated  = existing.withDescription(checkedDescription);
        updated = updated.withStatus(dto.status());
        repo.delete(existing);
        repo.save(updated);
        return updated;
    }

    public void deleteById(String id) {
        Todo existing = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No To-Do was found under this id."));
        repo.delete(existing);
    }

}
