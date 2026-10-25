
package org.example.controller;

import org.example.dto.RagRequest;
import org.example.dto.RagResponse;
import org.example.service.RagService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public RagResponse ask(@RequestBody RagRequest request) {

        if (request == null
                || request.question() == null
                || request.question().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Question must not be empty"
            );
        }

        int topK = request.topK() == null
                ? 3
                : request.topK();

        if (topK < 1 || topK > 3) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "topK must be between 1 and 3"
            );
        }

        return ragService.ask(
                request.question(),
                topK
        );
    }
}
