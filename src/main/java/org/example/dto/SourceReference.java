package org.example.dto;

public record SourceReference(String id,
                              String chunkId,
                              String source,
                                int chunkIndex,
                                int startOffset,
                                int endOffset,
                              double score,
                              String content) {
}
