package com.upload.document_upload_service.util;

import jakarta.persistence.AttributeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class FileDataJsonConverter implements AttributeConverter<List<byte[]>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<byte[]> fileData) {
        try {
            return objectMapper.writeValueAsString(fileData);
        } catch (Exception e) {
            throw new RuntimeException("Error converting file data to JSON", e);
        }
    }

    @Override
    public List<byte[]> convertToEntityAttribute(String fileDataJson) {
        try {
            return objectMapper.readValue(fileDataJson, new TypeReference<List<byte[]>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to file data", e);
        }
    }
}
