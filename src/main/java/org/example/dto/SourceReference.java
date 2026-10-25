package org.example.dto;

public record SourceReference(String id,
                              String source,
                              double score,
                              String content) {
}
