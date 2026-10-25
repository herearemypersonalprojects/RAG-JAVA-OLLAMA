package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OllamaEmbeddingResponse(String model,
                                      List<List<Double>> embeddings) {
}
