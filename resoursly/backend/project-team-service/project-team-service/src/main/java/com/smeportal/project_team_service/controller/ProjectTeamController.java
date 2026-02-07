package com.smeportal.project_team_service.controller;

import com.smeportal.project_team_service.dto.ManagerUpdateDTO;
import com.smeportal.project_team_service.dto.ProjectTeamUpdateDTO;
import com.smeportal.project_team_service.entity.ProjectTeam;
import com.smeportal.project_team_service.service.ProjectTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/project-team")
@CrossOrigin("*")
public class ProjectTeamController {

    @Autowired
    private ProjectTeamService service;

    @PostMapping("/create")
    public ProjectTeam createTeam(@RequestBody ProjectTeam request) {
        return service.createProjectTeam(request);
    }

    @GetMapping("/{id}")
    public Optional<ProjectTeam> getTeamById(@PathVariable Long id) {
        return service.getProjectTeamById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTeam(@PathVariable Long id) {
        return service.deleteProjectTeam(id);
    }

    @PatchMapping("/{id}")
    public ProjectTeam updateProjectTeam(@PathVariable Long id, @RequestBody ProjectTeamUpdateDTO dto) {
        return service.updateProjectTeam(id, dto);
    }

    //26-02

    // Get Manager Name at 0th index by projectId
    @GetMapping("/manager-name/{projectId}")
    public String getManagerName(@PathVariable String projectId) {
        return service.getManagerName(projectId);
    }

    // Get Manager Email at 0th index by projectId
    @GetMapping("/manager-email/{projectId}")
    public String getManagerEmail(@PathVariable String projectId) {
        return service.getManagerEmail(projectId);
    }

    // Update Project Team by projectId
    @PatchMapping("/updatepr/{projectId}")
    public ProjectTeam updateProjectTeamByProjectId(@PathVariable String projectId, @RequestBody ProjectTeamUpdateDTO dto) {
        return service.updateProjectTeamByProjectId(projectId, dto);
    }

    // Delete Project Team by projectId
    @DeleteMapping("/deletepr/{projectId}")
    public String deleteProjectTeamByProjectId(@PathVariable String projectId) {
        return service.deleteProjectTeamByProjectId(projectId);
    }

//    @PatchMapping("/update/manager-name/{projectId}")
//    public ProjectTeam updateManagerName(@PathVariable String projectId, @RequestBody String newName) {
//        return service.updateManagerName(projectId, newName);
//    }
//
//    @PatchMapping("/update/manager-email/{projectId}")
//    public ProjectTeam updateManagerEmail(@PathVariable String projectId, @RequestBody String newEmail) {
//        return service.updateManagerEmail(projectId, newEmail);
//    }

    @PatchMapping("/update/manager-name/{projectId}")
    public ProjectTeam updateManagerName(@PathVariable String projectId, @RequestBody ManagerUpdateDTO dto) {
        return service.updateManagerName(projectId, dto.getNewName());
    }

    @PatchMapping("/update/manager-email/{projectId}")
    public ProjectTeam updateManagerEmail(@PathVariable String projectId, @RequestBody ManagerUpdateDTO dto) {
        return service.updateManagerEmail(projectId, dto.getNewEmail());
    }






}
