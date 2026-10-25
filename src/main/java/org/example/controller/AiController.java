package org.example.controller;

import org.example.client.OllamaClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final OllamaClient ollamaClient;

    public AiController(OllamaClient ollamaClient) {
        this.ollamaClient = ollamaClient;
    }

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question is required");
        }
        String answer = ollamaClient.chat(question);
        return Map.of("answer", answer);
    }
}
