package com.smeportal.feedbackservice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smeportal.feedbackservice.model.FeedbackDetail;
import jakarta.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

public class Feedback2JsonConverter implements AttributeConverter<List<FeedbackDetail>, String> {

    private final ObjectMapper objectMapper;

    public Feedback2JsonConverter() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Ensure proper LocalDate serialization
    }

    @Override
    public String convertToDatabaseColumn(List<FeedbackDetail> feedback2) {
        try {
            if (feedback2 == null || feedback2.isEmpty()) {
                return "[]"; // Return empty JSON array
            }
            return objectMapper.writeValueAsString(feedback2);
        } catch (Exception e) {
            throw new RuntimeException("Error converting feedback2 to JSON", e);
        }
    }

    @Override
    public List<FeedbackDetail> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, FeedbackDetail.class));
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to feedback2", e);
        }
    }
}
