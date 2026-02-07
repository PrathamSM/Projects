package com.smeproject.manage_project_service.controller;

import com.smeproject.manage_project_service.dto.ManageProjectDTO;
import com.smeproject.manage_project_service.dto.ProjectCombinedDTO;
import com.smeproject.manage_project_service.service.ManageProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/managepro")
@CrossOrigin("*")
public class ManageProjectController {

    @Autowired
    private ManageProjectService manageProjectService;

    @GetMapping("/{projectId}")
    public ManageProjectDTO getProjectDetails(@PathVariable String projectId) {
        return manageProjectService.getProjectDetails(projectId);
    }

    @DeleteMapping("/delete-all/{projectId}")
    public ResponseEntity<String> deleteAllData(@PathVariable String projectId) {
        String response = manageProjectService.deleteAllDataByProjectId(projectId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details")
    public ResponseEntity<List<ProjectCombinedDTO>> getAllProjectDetails() {
        List<ProjectCombinedDTO> projectDetails = manageProjectService.getAllProjectDetails();
        return ResponseEntity.ok(projectDetails);
    }
}
