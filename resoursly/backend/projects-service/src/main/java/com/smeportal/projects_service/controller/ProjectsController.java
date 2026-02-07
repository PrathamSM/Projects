package com.smeportal.projects_service.controller;

import com.smeportal.projects_service.dto.ProjectDetailsDTO;
import com.smeportal.projects_service.dto.ProjectIdNameDTO;
import com.smeportal.projects_service.dto.ProjectUpdateDTO;
import com.smeportal.projects_service.entity.Projects;
import com.smeportal.projects_service.service.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects")
@CrossOrigin("*")
public class ProjectsController {

    @Autowired
    private ProjectsService projectsService;

    @PostMapping
    public ResponseEntity<Projects> createProject(@RequestBody Projects project) {
        Projects savedProject = projectsService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projects> getProjectById(@PathVariable("id") String projectId) {
        return ResponseEntity.ok(projectsService.getProjectById(projectId));
    }

    @GetMapping
    public ResponseEntity<List<Projects>> getAllProjects() {
        return ResponseEntity.ok(projectsService.getAllProjects());
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Projects> updateProject(@PathVariable("projectId") String projectId, @RequestBody ProjectUpdateDTO projectDetails) {
        Projects updatedProject = projectsService.updateProject(projectId, projectDetails);
        return ResponseEntity.ok(updatedProject);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") String projectId) {
        projectsService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/next-id")
    public ResponseEntity<String> getNextProjectId() {
        return ResponseEntity.ok(projectsService.getNextProjectId());
    }


    @GetMapping("/validate-project-id")
    public ResponseEntity<Boolean> validateProjectId(@RequestParam("projectId") String projectId) {
        boolean exists = projectsService.existsByProjectId(projectId);
        return ResponseEntity.ok(exists);
    }

    //26 02 controller

    @GetMapping("/name/{projectId}")
    public ResponseEntity<String> getProjectNameById(@PathVariable("projectId") String projectId) {
        return ResponseEntity.ok(projectsService.getProjectNameById(projectId));
    }

    @GetMapping("/start-date/{projectId}")
    public ResponseEntity<LocalDate> getProjectStartDateById(@PathVariable("projectId") String projectId) {
        return ResponseEntity.ok(projectsService.getProjectStartDateById(projectId));
    }

    @GetMapping("/end-date/{projectId}")
    public ResponseEntity<LocalDate> getProjectEndDateById(@PathVariable("projectId") String projectId) {
        return ResponseEntity.ok(projectsService.getProjectEndDateById(projectId));
    }


    @GetMapping("/currency/{projectId}")
    public ResponseEntity<String> getCurrencyByProjectId(@PathVariable("projectId") String projectId) {
        return ResponseEntity.ok(projectsService.getCurrencyByProjectId(projectId));
    }

    @GetMapping("/budget/{projectId}")
    public ResponseEntity<Double> getPlannedBudgetByProjectId(@PathVariable("projectId") String projectId) {
        return ResponseEntity.ok(projectsService.getPlannedBudgetByProjectId(projectId));
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Projects> updateProjectById(
            @PathVariable("projectId") String projectId,
            @RequestBody ProjectUpdateDTO projectDetails) {
        Projects updatedProject = projectsService.updateProjectById(projectId, projectDetails);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping("/pronameid")
    public List<ProjectIdNameDTO> getAllProjectIdsAndNames() {
        return projectsService.getAllProjectIdsAndNames();
    }

    @GetMapping("/contactperson/{projectId}")
    public ResponseEntity<String> getProjectContactPerson(@PathVariable String projectId) {
        return ResponseEntity.ok(projectsService.getProjectContactPersonById(projectId));
    }

    @GetMapping("/contactemail/{projectId}")
    public ResponseEntity<String> getProjectContactEmail(@PathVariable String projectId) {
        return ResponseEntity.ok(projectsService.getProjectContactEmailById(projectId));
    }

    @GetMapping("/contactphone/{projectId}")
    public ResponseEntity<String> getProjectContactPhone(@PathVariable String projectId) {
        return ResponseEntity.ok(projectsService.getProjectContactPhoneById(projectId));
    }

    @GetMapping("/details")
    public ResponseEntity<List<ProjectDetailsDTO>> getAllProjectDetails() {
        List<ProjectDetailsDTO> projectDetails = projectsService.getAllProjectDetails();
        return ResponseEntity.ok(projectDetails);
    }


    //To count total Project
    @GetMapping("/count")
    public Long getProjectCount() {
        return projectsService.getProjectCount();
    }

    @GetMapping("/{projectId}/proname")
    public String getProjectName(@PathVariable String projectId) {
        return projectsService.getProjectNameById(projectId);
    }

    @GetMapping("/total-count")
    public ResponseEntity<?> getProjectCountTotal() {    
        long count = projectsService.getProjectCount();    
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("projectsCount", count));
        }
}