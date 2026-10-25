package org.example.client;

import org.example.dto.ChatMessage;
import org.example.dto.OllamaChatRequest;
import org.example.dto.OllamaChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class OllamaClient {
    private final RestClient restClient;
    private final String chatModel;

    public OllamaClient(
            @Value("${ollama.base-url}") String baseUrl,
            @Value("${ollama.chat-model}") String chatModel) {

        this.restClient = RestClient.create(baseUrl);
        this.chatModel = chatModel;
    }

    public String chat(String question) {
        return chat("You are a helpful assistant.", question);
    }

    public String chat(String systemPrompt, String question) {

        List<ChatMessage> messages = List.of(
                new ChatMessage("system", systemPrompt),
                new ChatMessage("user", question)
        );

        OllamaChatRequest request = new OllamaChatRequest(
                        chatModel,
                        messages,
                        false
                );

        OllamaChatResponse response = restClient.post()
                .uri("/api/chat")
                .body(request)
                .retrieve()
                .body(OllamaChatResponse.class);

        if (response == null || response.message() == null) {
            throw new IllegalStateException(
                    "Ollama returned an invalid response"
            );
        }

        return response.message().content();
    }
}
