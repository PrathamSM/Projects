package com.upload.document_upload_service.util;

import jakarta.persistence.AttributeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class FileTypeJsonConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> fileTypes) {
        try {
            return objectMapper.writeValueAsString(fileTypes);
        } catch (Exception e) {
            throw new RuntimeException("Error converting file types to JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String fileTypesJson) {
        try {
            return objectMapper.readValue(fileTypesJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to file types", e);
        }
    }
}
