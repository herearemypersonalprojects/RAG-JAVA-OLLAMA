package org.example.model;

public record SearchResult(String source,
                           String content,
                           double score) {
}
