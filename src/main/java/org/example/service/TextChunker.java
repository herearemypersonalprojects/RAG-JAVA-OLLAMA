
package org.example.service;

import org.example.model.DocumentChunk;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextChunker {

    public List<DocumentChunk> chunk(
            String source,
            String text,
            int chunkSize,
            int overlap) {

        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException(
                    "Source must not be empty"
            );
        }

        if (text == null) {
            throw new IllegalArgumentException(
                    "Text must not be null"
            );
        }

        if (chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "chunkSize must be positive"
            );
        }

        if (overlap < 0 || overlap >= chunkSize) {
            throw new IllegalArgumentException(
                    "Overlap must be between 0 and chunkSize - 1"
            );
        }

        if (text.isBlank()) {
            return List.of();
        }

        List<DocumentChunk> chunks = new ArrayList<>();

        int start = 0;
        int chunkIndex = 0;

        while (start < text.length()) {

            int end = Math.min(
                    start + chunkSize,
                    text.length()
            );

            String content = text.substring(start, end);

            chunks.add(new DocumentChunk(
                    source + "#" + chunkIndex,
                    source,
                    chunkIndex,
                    start,
                    end,
                    content
            ));

            if (end == text.length()) {
                break;
            }

            start = end - overlap;
            chunkIndex++;
        }

        return List.copyOf(chunks);
    }
}
