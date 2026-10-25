package org.example.dto;

public record SearchRequest(String question,
                            Integer topK) {
}
