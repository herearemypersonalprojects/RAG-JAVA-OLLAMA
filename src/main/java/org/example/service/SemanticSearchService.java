
package org.example.service;

import org.example.client.EmbeddingClient;
import org.example.model.IndexedDocument;
import org.example.model.SearchResult;
import org.example.util.VectorUtils;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SemanticSearchService {

    private final DocumentLoader documentLoader;
    private final EmbeddingClient embeddingClient;

    private volatile List<IndexedDocument> index = List.of();

    public SemanticSearchService(
            DocumentLoader documentLoader,
            EmbeddingClient embeddingClient) {

        this.documentLoader = documentLoader;
        this.embeddingClient = embeddingClient;
    }

    // STEP 1: Index all documents when the app starts
    @EventListener(ApplicationReadyEvent.class)
    public void indexDocuments() throws IOException {

        List<Resource> resources = documentLoader.loadDocuments();
        List<IndexedDocument> newIndex = new ArrayList<>();

        for (Resource resource : resources) {

            String source = resource.getFilename();
            String content = documentLoader.readContent(resource);

            if (content.isBlank()) {
                continue;
            }

            System.out.println("Indexing: " + source);

            List<Double> vector = embeddingClient.embed(content);

            IndexedDocument document = new IndexedDocument(
                    source,
                    content,
                    List.copyOf(vector)
            );

            newIndex.add(document);
        }

        if (newIndex.isEmpty()) {
            throw new IllegalStateException(
                    "No documents were indexed"
            );
        }

        index = List.copyOf(newIndex);

        System.out.println(
                "Indexing completed: " + index.size() + " documents"
        );
    }

    // STEP 2: Search for the most relevant documents
    public List<SearchResult> search(String question, int topK) {

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException(
                    "Question must not be empty"
            );
        }

        if (topK <= 0) {
            throw new IllegalArgumentException(
                    "topK must be greater than zero"
            );
        }

        List<IndexedDocument> documents = index;

        if (documents.isEmpty()) {
            throw new IllegalStateException(
                    "Index is not ready"
            );
        }

        List<Double> questionVector = embeddingClient.embed(question);

        List<SearchResult> results = new ArrayList<>();

        for (IndexedDocument document : documents) {

            double similarity = VectorUtils.cosineSimilarity(
                    questionVector,
                    document.embeddings()
            );

            results.add(new SearchResult(
                    document.source(),
                    document.content(),
                    similarity
            ));
        }

        results.sort(
                Comparator.comparingDouble(SearchResult::score)
                        .reversed()
        );

        return List.copyOf(
                results.subList(0, Math.min(topK, results.size()))
        );
    }
}
