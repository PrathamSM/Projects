package com.smeproject.manage_project_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "assign-resource-service", url = "http://localhost:8045/api/assignments")
public interface AssignResourceClient {
    @GetMapping("/count/{projectId}")
    ResponseEntity<Integer> getProfileCountByProjectId(@PathVariable String projectId);

    @GetMapping("/profile-count")
    List<Map<String, Object>> getProjectProfileCount();
}

