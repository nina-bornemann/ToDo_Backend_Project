package com.ninabornemann.todo_backend_project.models;

import lombok.With;

public record Todo(String id, @With String description, @With Status status) {
}
