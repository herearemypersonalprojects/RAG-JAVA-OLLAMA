
package org.example.service;

import org.example.client.EmbeddingClient;
import org.example.model.DocumentChunk;
import org.example.model.IndexedDocument;
import org.example.model.SearchResult;
import org.example.util.VectorUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class SemanticSearchService {

    private final DocumentLoader documentLoader;
    private final TextChunker textChunker;
    private final EmbeddingClient embeddingClient;

    private final int chunkSize;
    private final int chunkOverlap;

    private volatile List<IndexedDocument> index = List.of();

    public SemanticSearchService(
            DocumentLoader documentLoader,
            TextChunker textChunker,
            EmbeddingClient embeddingClient,
            @Value("${rag.chunk-size}") int chunkSize,
            @Value("${rag.chunk-overlap}") int chunkOverlap) {

        this.documentLoader = documentLoader;
        this.textChunker = textChunker;
        this.embeddingClient = embeddingClient;

        if (chunkSize <= 0
                || chunkOverlap < 0
                || chunkOverlap >= chunkSize) {
            throw new IllegalArgumentException(
                    "Invalid chunking configuration"
            );
        }

        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void indexDocuments() throws IOException {

        List<Resource> resources = documentLoader.loadDocuments();
        List<IndexedDocument> newIndex = new ArrayList<>();

        for (Resource resource : resources) {

            String source = resource.getFilename();
            String content = documentLoader.readContent(resource);

            List<DocumentChunk> chunks = textChunker.chunk(
                    source,
                    content,
                    chunkSize,
                    chunkOverlap
            );

            System.out.printf(
                    "Document: %s - chunks: %d%n",
                    source,
                    chunks.size()
            );

            for (DocumentChunk chunk : chunks) {

                List<Double> vector =
                        embeddingClient.embed(chunk.content());

                IndexedDocument indexedDocument =
                        new IndexedDocument(
                                chunk,
                                List.copyOf(vector)
                        );

                newIndex.add(indexedDocument);

                System.out.println(
                        "Indexed chunk: " + chunk.chunkId()
                );
            }
        }

        if (newIndex.isEmpty()) {
            throw new IllegalStateException(
                    "No chunks were indexed"
            );
        }

        index = List.copyOf(newIndex);

        System.out.printf(
                "Indexing completed: %d chunks%n",
                index.size()
        );
    }

    public List<SearchResult> search(String question, int topK) {

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException(
                    "Question must not be empty"
            );
        }

        if (topK <= 0) {
            throw new IllegalArgumentException(
                    "topK must be positive"
            );
        }

        List<IndexedDocument> documents = index;

        if (documents.isEmpty()) {
            throw new IllegalStateException(
                    "Index is not ready"
            );
        }

        List<Double> queryVector =
                embeddingClient.embed(question);

        List<SearchResult> results = new ArrayList<>();

        for (IndexedDocument document : documents) {

            double score = VectorUtils.cosineSimilarity(
                    queryVector,
                    document.embedding()
            );

            DocumentChunk chunk = document.chunk();

            results.add(new SearchResult(
                    chunk.chunkId(),
                    chunk.source(),
                    chunk.chunkIndex(),
                    chunk.startOffset(),
                    chunk.endOffset(),
                    chunk.content(),
                    score
            ));
        }

        results.sort(
                Comparator.comparingDouble(SearchResult::score)
                        .reversed()
                        .thenComparing(SearchResult::chunkId)
        );

        return List.copyOf(
                results.subList(
                        0,
                        Math.min(topK, results.size())
                )
        );
    }

    public int getIndexedChunkCount() {
        return index.size();
    }
}
