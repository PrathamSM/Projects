package com.smeportal.assignresourceservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "projects-service", url = "http://localhost:8040")
public interface ProjectServiceClient {

    @GetMapping("/projects/count")
    Long getProjectCount();

    @GetMapping("/{projectId}/proname")
    String getProjectName(@PathVariable("projectId") String projectId);
}



