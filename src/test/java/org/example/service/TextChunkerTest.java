
package org.example.service;

import org.example.model.DocumentChunk;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextChunkerTest {

    private final TextChunker chunker = new TextChunker();

    @Test
    void shouldCreateOverlappingChunks() {

        String text = "ABCDEFGHIJKLMNOPQRST";

        List<DocumentChunk> chunks =
                chunker.chunk("test.txt", text, 10, 3);

        assertEquals(3, chunks.size());

        assertEquals("ABCDEFGHIJ", chunks.get(0).content());
        assertEquals("HIJKLMNOPQ", chunks.get(1).content());
        assertEquals("OPQRST", chunks.get(2).content());

        assertEquals(0, chunks.get(0).startOffset());
        assertEquals(10, chunks.get(0).endOffset());

        assertEquals(7, chunks.get(1).startOffset());
        assertEquals(17, chunks.get(1).endOffset());

        assertEquals(14, chunks.get(2).startOffset());
        assertEquals(20, chunks.get(2).endOffset());
    }

    @Test
    void shouldKeepSmallDocumentInOneChunk() {

        List<DocumentChunk> chunks =
                chunker.chunk("short.txt", "Bonjour", 100, 20);

        assertEquals(1, chunks.size());
        assertEquals("Bonjour", chunks.get(0).content());
        assertEquals("short.txt#0", chunks.get(0).chunkId());
    }

    @Test
    void shouldReturnNoChunkForBlankText() {

        List<DocumentChunk> chunks =
                chunker.chunk("empty.txt", "   ", 100, 20);

        assertTrue(chunks.isEmpty());
    }

    @Test
    void shouldRejectInvalidOverlap() {

        assertThrows(
                IllegalArgumentException.class,
                () -> chunker.chunk("test.txt", "Bonjour", 10, 10)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> chunker.chunk("test.txt", "Bonjour", 10, -1)
        );
    }

    @Test
    void shouldPreserveOriginalTextAtOffsets() {

        String text = "Le salarié bénéficie de congés payés.";

        List<DocumentChunk> chunks =
                chunker.chunk("conges.txt", text, 15, 5);

        for (DocumentChunk chunk : chunks) {

            String originalPart = text.substring(
                    chunk.startOffset(),
                    chunk.endOffset()
            );
            System.out.println("Chunk: " + chunk.content());
            assertEquals(originalPart, chunk.content());
        }
    }


    @Test
    void shouldPreserveOverlapBetweenAdjacentChunks() {

        String text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        List<DocumentChunk> chunks =
                chunker.chunk("alphabet.txt", text, 10, 4);

        for (int i = 0; i < chunks.size() - 1; i++) {

            DocumentChunk current = chunks.get(i);
            DocumentChunk next = chunks.get(i + 1);

            int overlapLength =
                    current.endOffset() - next.startOffset();

            assertEquals(4, overlapLength);

            String endOfCurrent = current.content()
                    .substring(current.content().length() - 4);

            String beginningOfNext = next.content()
                    .substring(0, 4);

            assertEquals(endOfCurrent, beginningOfNext);
        }
    }

}
