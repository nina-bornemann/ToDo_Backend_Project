package com.ninabornemann.todo_backend_project.repository;

import com.ninabornemann.todo_backend_project.models.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface TodoRepo extends MongoRepository<Todo, String> {

    List<Todo> allTodos = new ArrayList<>();
}
