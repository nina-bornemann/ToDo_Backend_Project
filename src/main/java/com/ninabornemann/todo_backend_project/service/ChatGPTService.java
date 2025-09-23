package com.ninabornemann.todo_backend_project.service;

import com.ninabornemann.todo_backend_project.models.OpenAiMessage;
import com.ninabornemann.todo_backend_project.models.OpenAiRequest;
import com.ninabornemann.todo_backend_project.models.OpenAiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ChatGPTService {

    private final RestClient restClient;

    public ChatGPTService(RestClient.Builder restClientBuilder, @Value("${OPENAI_API_KEY}") String apiKey) {
        this.restClient = restClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public String checkSpelling(String text) {
        OpenAiResponse response = restClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new OpenAiRequest("gpt-5", List.of(
                        new OpenAiMessage("user",
                                "Please check for spelling errors ang give me only the corrected text."),
                        new OpenAiMessage("user", text))))
                .retrieve()
                .body(OpenAiResponse.class);
        return response.choices().getFirst().message().content();
    }
}
