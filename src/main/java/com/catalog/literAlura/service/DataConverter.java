package com.catalog.literAlura.service;

import com.catalog.literAlura.model.AuthorData;
import com.catalog.literAlura.model.BookData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataConverter implements IDataConverter {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T getData(String json, Class<T> clazz) {
        try {
            JsonNode node = mapper.readTree(json);
            if (node == null) {
                // System.out.println("JSON response is null");
                return null;
            }
            if (!node.has("results")) {
                // System.out.println("JSON response does not have 'results' field: " + json);
                return null;
            }
            if (!node.get("results").isArray() || node.get("results").size() == 0) {
                // System.out.println("No results found in JSON response: " + json);
                return null;
            }
            if (clazz == BookData.class) {
                var firstResult = node.get("results").get(0);
                if (firstResult == null) {
                    // System.out.println("First result in 'results' is null: " + json);
                    return null;
                }
                return mapper.treeToValue(firstResult, clazz);
            } else if (clazz == AuthorData.class) {
                var firstAuthor = node.get("results").get(0).get("authors").get(0);
                if (firstAuthor == null) {
                    // System.out.println("First author in 'results' is null: " + json);
                    return null;
                }
                return mapper.treeToValue(firstAuthor, clazz);
            } else {
                return mapper.readValue(json, clazz);
            }
        } catch (JsonProcessingException e) {
            // System.out.println("Error processing JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
