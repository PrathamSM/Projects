package com.smeportal.project_team_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "projects-service", url = "http://localhost:8040/projects")
public interface ProjectServiceClient {
    @GetMapping("/validate-project-id")
    Boolean validateProjectId(@RequestParam("projectId") String projectId);
}
