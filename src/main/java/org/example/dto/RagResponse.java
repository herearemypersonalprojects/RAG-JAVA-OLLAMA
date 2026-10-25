package org.example.dto;

import java.util.List;

public record RagResponse(String question,
                          String answer,
                          List<SourceReference> sources) {
}
