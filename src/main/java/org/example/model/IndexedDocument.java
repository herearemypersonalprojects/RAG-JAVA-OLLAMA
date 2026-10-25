package org.example.model;

import java.util.List;

public record IndexedDocument(DocumentChunk chunk,
                              List<Double> embedding) {
}
