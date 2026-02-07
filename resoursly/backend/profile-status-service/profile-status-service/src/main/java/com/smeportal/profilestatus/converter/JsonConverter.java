package com.smeportal.profilestatus.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smeportal.profilestatus.model.Availability;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonConverter implements AttributeConverter<Availability, String> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);;

    @Override
    public String convertToDatabaseColumn(Availability attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting Availability to JSON", e);
        }
    }

    @Override
    public Availability convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, Availability.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to Availability", e);
        }
    }
}
