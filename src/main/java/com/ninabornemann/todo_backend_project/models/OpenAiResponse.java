package com.ninabornemann.todo_backend_project.models;

import java.util.List;

public record OpenAiResponse(List<OpenAiChoice> choices) {
}
