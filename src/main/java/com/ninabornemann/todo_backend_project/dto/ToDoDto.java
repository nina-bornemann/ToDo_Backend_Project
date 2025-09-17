package com.ninabornemann.todo_backend_project.dto;

import com.ninabornemann.todo_backend_project.models.Status;

public record ToDoDto(String description, Status status) {

}
