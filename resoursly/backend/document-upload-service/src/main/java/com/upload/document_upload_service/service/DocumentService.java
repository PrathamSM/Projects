package com.upload.document_upload_service.service;

import com.upload.document_upload_service.entity.Document;
import com.upload.document_upload_service.exception.FileProcessingException;
import com.upload.document_upload_service.feign.ProjectServiceClient;
import com.upload.document_upload_service.repository.DocumentRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectServiceClient projectServiceClient;

    public DocumentService(DocumentRepository documentRepository, ProjectServiceClient projectServiceClient) {
        this.documentRepository = documentRepository;
        this.projectServiceClient = projectServiceClient;
    }

    public Document uploadDocuments(String projectId, List<MultipartFile> files) throws FileProcessingException {
        Boolean isValidProject = projectServiceClient.validateProjectId(projectId);
        if (!isValidProject) {
            throw new FileProcessingException("Invalid Project ID: " + projectId);
        }

        try {
            List<String> fileNames = new ArrayList<>();
            List<String> fileTypes = new ArrayList<>();
            List<Long> fileSizes = new ArrayList<>();
            List<byte[]> fileData = new ArrayList<>();

            for (MultipartFile file : files) {
                fileNames.add(file.getOriginalFilename());
                fileTypes.add(file.getContentType());
                fileSizes.add(file.getSize());
                fileData.add(file.getBytes());
            }

            Document document = Document.builder()
                    .projectId(projectId)
                    .fileNames(fileNames)
                    .fileTypes(fileTypes)
                    .fileSizes(fileSizes)
                    .fileData(fileData)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            return documentRepository.save(document);
        } catch (IOException e) {
            throw new FileProcessingException("Error processing files: " + e.getMessage());
        }
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public List<Document> getDocumentsByProjectId(String projectId) {
        return documentRepository.findByProjectId(projectId);
    }

    // **View Document Implementation**
    public ResponseEntity<ByteArrayResource> viewDocumentById(Long id, int index) {
        Optional<Document> documentOptional = documentRepository.findById(id);

        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();

            if (index >= 0 && index < document.getFileData().size()) {
                byte[] fileBytes = document.getFileData().get(index);
                String fileType = document.getFileTypes().get(index);

                ByteArrayResource resource = new ByteArrayResource(fileBytes);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileType))
                        .body(resource);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // **Delete Document Implementation**
    public boolean deleteDocument(Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deleteFileByIndex(Long id, int index) {
        Optional<Document> documentOptional = documentRepository.findById(id);

        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();

            if (index >= 0 && index < document.getFileData().size()) {
                // Remove the file data at the specified index
                document.getFileNames().remove(index);
                document.getFileTypes().remove(index);
                document.getFileSizes().remove(index);
                document.getFileData().remove(index);

                // If all files are deleted, remove the entire document entry
                if (document.getFileData().isEmpty()) {
                    documentRepository.deleteById(id);
                } else {
                    documentRepository.save(document);
                }
                return true;
            }
        }
        return false;
    }

    public boolean deleteDocumentsByProjectId(String projectId) {
        List<Document> documents = documentRepository.findByProjectId(projectId);
        if (!documents.isEmpty()) {
            documentRepository.deleteAll(documents);
            return true;
        }
        return false;
    }



}
