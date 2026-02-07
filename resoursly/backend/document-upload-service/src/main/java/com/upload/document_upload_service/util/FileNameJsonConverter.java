package com.upload.document_upload_service.util;

import jakarta.persistence.AttributeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class FileNameJsonConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> fileNames) {
        try {
            return objectMapper.writeValueAsString(fileNames);
        } catch (Exception e) {
            throw new RuntimeException("Error converting file names to JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String fileNamesJson) {
        try {
            return objectMapper.readValue(fileNamesJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to file names", e);
        }
    }
}
