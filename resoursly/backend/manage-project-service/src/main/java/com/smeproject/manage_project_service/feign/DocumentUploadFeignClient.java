package com.smeproject.manage_project_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "document-upload-service", url = "http://localhost:8043/api/documents")
public interface DocumentUploadFeignClient {
    @DeleteMapping("/project/{projectId}")
    ResponseEntity<String> deleteDocuments(@PathVariable String projectId);
}
