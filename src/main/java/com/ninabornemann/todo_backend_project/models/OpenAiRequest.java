package com.ninabornemann.todo_backend_project.models;

import java.util.List;

public record OpenAiRequest(String model,
                            List<OpenAiMessage> messages) {
}
