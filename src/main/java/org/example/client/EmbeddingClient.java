
package org.example.client;

import org.example.dto.OllamaEmbeddingRequest;
import org.example.dto.OllamaEmbeddingResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class EmbeddingClient {

    private final RestClient restClient;
    private final String embeddingModel;

    public EmbeddingClient(
            @Value("${ollama.base-url}") String baseUrl,
            @Value("${ollama.embedding-model}") String embeddingModel) {

        this.restClient = RestClient.create(baseUrl);
        this.embeddingModel = embeddingModel;
    }

    public List<Double> embed(String text) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Text must not be empty"
            );
        }

        // 1. Create the request
        OllamaEmbeddingRequest request =
                new OllamaEmbeddingRequest(
                        embeddingModel,
                        text
                );

        // 2. Call the Ollama API
        OllamaEmbeddingResponse response =
                restClient.post()
                        .uri("/api/embed")
                        .body(request)
                        .retrieve()
                        .body(OllamaEmbeddingResponse.class);

        // 3. Validate the response
        if (response == null
                || response.embeddings() == null
                || response.embeddings().isEmpty()) {

            throw new IllegalStateException(
                    "Ollama returned no embeddings"
            );
        }

        // 4. Return the first vector
        List<Double> vector = response.embeddings().get(0);

        if (vector == null || vector.isEmpty()) {
            throw new IllegalStateException(
                    "Ollama returned an empty vector"
            );
        }

        return vector;
    }
}
