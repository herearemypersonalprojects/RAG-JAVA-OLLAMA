
package org.example.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
public class DocumentLoader {

    private final ResourcePatternResolver resourceResolver;

    public DocumentLoader(ResourcePatternResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    public List<Resource> loadDocuments() throws IOException {

        Resource[] resources = resourceResolver.getResources(
                "classpath*:knowledge/*.txt"
        );

        List<Resource> documents = new ArrayList<>(
                Arrays.asList(resources)
        );

        documents.sort(
                Comparator.comparing(Resource::getFilename)
        );

        return documents;
    }

    public String readContent(Resource resource) throws IOException {

        try (var inputStream = resource.getInputStream()) {

            return new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }
    }
}
