package com.ninabornemann.todo_backend_project.service;

import com.ninabornemann.todo_backend_project.repository.TodoRepo;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService {

    public String randomId() {
        return UUID.randomUUID().toString();
    }


}
