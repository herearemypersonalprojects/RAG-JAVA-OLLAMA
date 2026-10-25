
package org.example.controller;

import org.example.dto.SearchRequest;
import org.example.model.SearchResult;
import org.example.service.SemanticSearchService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SemanticSearchService semanticSearchService;

    public SearchController(SemanticSearchService semanticSearchService) {
        this.semanticSearchService = semanticSearchService;
    }

    @PostMapping
    public List<SearchResult> search(
            @RequestBody SearchRequest request) {

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

        if (topK <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "topK must be greater than zero"
            );
        }

        return semanticSearchService.search(
                request.question(),
                topK
        );
    }
}
