package com.ninabornemann.todo_backend_project.models;

import java.time.Instant;

public record ErrorMessage(String errorMessage, Instant instant) {
}
