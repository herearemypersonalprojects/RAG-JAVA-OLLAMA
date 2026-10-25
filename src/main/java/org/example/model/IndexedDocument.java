package org.example.model;

import java.util.List;

public record IndexedDocument(String source,
                              String content,
                              List<Double> embeddings) {
}
