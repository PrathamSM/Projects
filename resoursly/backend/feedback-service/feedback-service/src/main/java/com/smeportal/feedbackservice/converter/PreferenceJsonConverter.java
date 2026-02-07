package com.smeportal.feedbackservice.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smeportal.feedbackservice.model.Preference;
import jakarta.persistence.AttributeConverter;

import java.util.ArrayList;
import java.util.List;

public class PreferenceJsonConverter implements AttributeConverter<List<Preference>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Convert List<Preference> to JSON String for storing in the database
    @Override
    public String convertToDatabaseColumn(List<Preference> attribute) {
        try {
            if (attribute == null || attribute.isEmpty()) {
                return "[]"; // Return empty JSON array if preferences are null/empty
            }
            String json = objectMapper.writeValueAsString(attribute);
            System.out.println("Converted preferences to JSON: " + json);
            return json;
        } catch (Exception e) {
            throw new RuntimeException("Error converting preferences to JSON", e);
        }
    }

    // Convert JSON String from the database to List<Preference>
    @Override
    public List<Preference> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return new ArrayList<>(); // Return empty list if no data is present
            }
            List<Preference> preferences = objectMapper.readValue(dbData, new TypeReference<List<Preference>>() {});
            System.out.println("Converted JSON to preferences: " + preferences);
            return preferences;
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to preferences", e);
        }
    }
}