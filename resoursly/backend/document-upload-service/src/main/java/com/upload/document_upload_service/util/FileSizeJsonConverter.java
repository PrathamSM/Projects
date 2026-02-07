package com.upload.document_upload_service.util;

import jakarta.persistence.AttributeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class FileSizeJsonConverter implements AttributeConverter<List<Long>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Long> fileSizes) {
        try {
            return objectMapper.writeValueAsString(fileSizes);
        } catch (Exception e) {
            throw new RuntimeException("Error converting file sizes to JSON", e);
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String fileSizesJson) {
        try {
            return objectMapper.readValue(fileSizesJson, new TypeReference<List<Long>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to file sizes", e);
        }
    }
}
