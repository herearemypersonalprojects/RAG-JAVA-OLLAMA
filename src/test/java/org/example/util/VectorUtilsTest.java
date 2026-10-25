
package org.example.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VectorUtilsTest {

    @Test
    void shouldReturnOneForIdenticalVectors() {

        List<Double> a = List.of(1.0, 2.0, 3.0);
        List<Double> b = List.of(1.0, 2.0, 3.0);

        double similarity = VectorUtils.cosineSimilarity(a, b);

        assertEquals(1.0, similarity, 0.000001);
    }

    @Test
    void shouldReturnZeroForOrthogonalVectors() {

        List<Double> a = List.of(1.0, 0.0);
        List<Double> b = List.of(0.0, 1.0);

        double similarity = VectorUtils.cosineSimilarity(a, b);

        assertEquals(0.0, similarity, 0.000001);
    }

    @Test
    void shouldReturnMinusOneForOppositeVectors() {

        List<Double> a = List.of(1.0, 0.0);
        List<Double> b = List.of(-1.0, 0.0);

        double similarity = VectorUtils.cosineSimilarity(a, b);

        assertEquals(-1.0, similarity, 0.000001);
    }

    @Test
    void shouldRejectDifferentDimensions() {

        List<Double> a = List.of(1.0, 2.0);
        List<Double> b = List.of(1.0, 2.0, 3.0);

        assertThrows(
                IllegalArgumentException.class,
                () -> VectorUtils.cosineSimilarity(a, b)
        );
    }

    @Test
    void shouldRejectZeroVector() {

        List<Double> a = List.of(0.0, 0.0);
        List<Double> b = List.of(1.0, 2.0);

        assertThrows(
                IllegalArgumentException.class,
                () -> VectorUtils.cosineSimilarity(a, b)
        );
    }
}
