package org.example.model;

public record SearchResult(String chunkId,
                           String source,
                           int chunkIndex,
                           int startOffset,
                           int endOffset,
                           String content,
                           double score) {
}
