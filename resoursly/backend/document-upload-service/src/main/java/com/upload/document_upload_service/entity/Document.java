package com.upload.document_upload_service.entity;

import com.upload.document_upload_service.util.FileDataJsonConverter;
import com.upload.document_upload_service.util.FileNameJsonConverter;
import com.upload.document_upload_service.util.FileSizeJsonConverter;
import com.upload.document_upload_service.util.FileTypeJsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private String projectId;

    @Convert(converter = FileNameJsonConverter.class)
    @Column(name = "file_names", columnDefinition = "JSON", nullable = false)
    private List<String> fileNames;

    @Convert(converter = FileTypeJsonConverter.class)
    @Column(name = "file_types", columnDefinition = "JSON", nullable = false)
    private List<String> fileTypes;

    @Convert(converter = FileSizeJsonConverter.class)
    @Column(name = "file_sizes", columnDefinition = "JSON", nullable = false)
    private List<Long> fileSizes;

    @Convert(converter = FileDataJsonConverter.class)
    @Column(name = "file_data", columnDefinition = "JSON", nullable = false)
    private List<byte[]> fileData;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }
}
