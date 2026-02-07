package com.smeportal.feedbackservice.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smeportal.feedbackservice.model.InterviewFeedback;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class InterviewFeedbackJsonConverter implements AttributeConverter<List<InterviewFeedback>, String> {
    private final ObjectMapper objectMapper;

    public InterviewFeedbackJsonConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Register Java Time Module
    }

    @Override
    public String convertToDatabaseColumn(List<InterviewFeedback> feedbackList) {
        try {
            return objectMapper.writeValueAsString(feedbackList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting InterviewFeedback list to JSON", e);
        }
    }

    @Override
    public List<InterviewFeedback> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, InterviewFeedback.class));
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to InterviewFeedback list", e);
        }
    }
}
