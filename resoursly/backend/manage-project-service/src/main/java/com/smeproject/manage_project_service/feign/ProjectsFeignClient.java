package com.smeproject.manage_project_service.feign;

import com.smeproject.manage_project_service.dto.ManageProjectDTO;
import com.smeproject.manage_project_service.dto.ManageProjectUpdateDTO;
import com.smeproject.manage_project_service.dto.ProjectDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "projects-service", url = "http://localhost:8040/projects")
public interface ProjectsFeignClient {

    @GetMapping("/name/{projectId}")
    String getProjectName(@PathVariable("projectId") String projectId);

    @GetMapping("/start-date/{projectId}")
    LocalDate getProjectStartDate(@PathVariable("projectId") String projectId);

    @GetMapping("/end-date/{projectId}")
    LocalDate getProjectEndDate(@PathVariable("projectId") String projectId);

    @GetMapping("/budget/{projectId}")
    Double getPlannedBudget(@PathVariable("projectId") String projectId);

    @GetMapping("/currency/{projectId}")
    String getCurrency(@PathVariable("projectId") String projectId);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProject(@PathVariable("id") String projectId);

    @PutMapping("/update/{projectId}")
    ResponseEntity<Void> updateProject(@PathVariable("projectId") String projectId,
                                       @RequestBody ManageProjectDTO updatedProject);

    @GetMapping("/contactperson/{projectId}")
    String getProjectContactPerson(@PathVariable String projectId);

    @GetMapping("/details")
    List<ProjectDetailsDTO> getAllProjectDetails();

}
