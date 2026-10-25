
package org.example.controller;

import org.example.client.EmbeddingClient;

import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/embeddings")
public class EmbeddingController {

    private final EmbeddingClient embeddingClient;

    public EmbeddingController(EmbeddingClient embeddingClient) {
        this.embeddingClient = embeddingClient;
    }

    @PostMapping
    public Map<String, Object> embed(
            @RequestBody Map<String, String> request) {

        String text = request.get("text");

        List<Double> vector = embeddingClient.embed(text);

        Map<String, Object> result = new LinkedHashMap<>();

        result.put("text", text);
        result.put("dimensions", vector.size());
        result.put("first10Values",
                vector.subList(0, Math.min(10, vector.size())));

        return result;
    }
}
