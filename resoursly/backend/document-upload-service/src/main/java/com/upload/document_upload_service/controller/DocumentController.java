package com.upload.document_upload_service.controller;

import com.upload.document_upload_service.entity.Document;
import com.upload.document_upload_service.exception.FileProcessingException;
import com.upload.document_upload_service.service.DocumentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin("*")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload/{projectId}")
    public ResponseEntity<Document> uploadDocuments(
            @PathVariable String projectId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            Document uploadedDocument = documentService.uploadDocuments(projectId, files);
            return ResponseEntity.ok(uploadedDocument);
        } catch (FileProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Document>> getDocumentsByProjectId(@PathVariable String projectId) {
        List<Document> documents = documentService.getDocumentsByProjectId(projectId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/download/{id}/{index}")
    public ResponseEntity<ByteArrayResource> downloadDocumentById(
            @PathVariable Long id,
            @PathVariable int index) {
        Optional<Document> documentOptional = documentService.getDocumentById(id);

        if (documentOptional.isPresent()) {
            Document document = documentOptional.get();

            if (index >= 0 && index < document.getFileData().size()) {
                byte[] fileBytes = document.getFileData().get(index);
                String fileName = document.getFileNames().get(index);
                String fileType = document.getFileTypes().get(index);

                ByteArrayResource resource = new ByteArrayResource(fileBytes);

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(resource);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // **View Document API**
    @GetMapping("/view/{id}/{index}")
    public ResponseEntity<ByteArrayResource> viewDocumentById(
            @PathVariable Long id,
            @PathVariable int index) {
        return documentService.viewDocumentById(id, index);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        boolean deleted = documentService.deleteDocument(id);
        if (deleted) {
            return ResponseEntity.ok("Document deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found.");
    }


    //delete by index

    @DeleteMapping("/delete/{id}/{index}")
    public ResponseEntity<String> deleteFileByIndex(@PathVariable Long id, @PathVariable int index) {
        boolean deleted = documentService.deleteFileByIndex(id, index);
        if (deleted) {
            return ResponseEntity.ok("File deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File does not exist of that index.");
    }

    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<String> deleteDocumentsByProjectId(@PathVariable String projectId) {
        boolean deleted = documentService.deleteDocumentsByProjectId(projectId);
        if (deleted) {
            return ResponseEntity.ok("All documents for project " + projectId + " deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No documents found for project " + projectId);
    }


}
