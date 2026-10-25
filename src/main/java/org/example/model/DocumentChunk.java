package org.example.model;

public record DocumentChunk(String chunkId,
                            String source,
                            int chunkIndex,
                            int startOffset,
                            int endOffset,
                            String content) {
}
