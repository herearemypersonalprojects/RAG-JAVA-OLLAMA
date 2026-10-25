
package org.example.util;

import java.util.List;

public final class VectorUtils {

    private VectorUtils() {
        // Utility class: no instances
    }

    public static double cosineSimilarity(
            List<Double> vectorA,
            List<Double> vectorB) {

        if (vectorA == null || vectorB == null
                || vectorA.isEmpty()
                || vectorA.size() != vectorB.size()) {
            throw new IllegalArgumentException(
                    "Vectors must have the same non-zero dimension"
            );
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.size(); i++) {

            double a = vectorA.get(i);
            double b = vectorB.get(i);

            dotProduct += a * b;
            normA += a * a;
            normB += b * b;
        }

        if (normA == 0.0 || normB == 0.0) {
            throw new IllegalArgumentException(
                    "Zero-length vector is not allowed"
            );
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
