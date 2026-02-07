package com.smeportal.project_team_service.service;

import com.smeportal.project_team_service.dto.ProjectTeamUpdateDTO;
import com.smeportal.project_team_service.entity.ProjectTeam;
import com.smeportal.project_team_service.feign.ProjectServiceClient;
import com.smeportal.project_team_service.repository.ProjectTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectTeamService {

    @Autowired
    private ProjectTeamRepository repository;

    @Autowired
    private ProjectServiceClient projectServiceClient;

    @Autowired
    private ProjectTeamMapper projectTeamMapper;

    public ProjectTeam createProjectTeam(ProjectTeam request) {
        Boolean isProjectValid = projectServiceClient.validateProjectId(request.getProjectId());

        if (Boolean.FALSE.equals(isProjectValid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid projectId: " + request.getProjectId());
        }

        return repository.save(request);
    }

    public Optional<ProjectTeam> getProjectTeamById(Long id) {
        return repository.findById(id);
    }

    public String deleteProjectTeam(Long id) {
        Optional<ProjectTeam> existingTeam = repository.findById(id);

        if (existingTeam.isPresent()) {
            repository.deleteById(id);
            return "Project Team with ID " + id + " deleted successfully.";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Team with ID " + id + " not found.");
        }
    }


    public ProjectTeam updateProjectTeam(Long teamId, ProjectTeamUpdateDTO dto) {
        ProjectTeam existingTeam = repository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project team with ID " + teamId + " not found."));

        projectTeamMapper.updateProjectTeamFromDto(dto, existingTeam);
        return repository.save(existingTeam);
    }

    //26-02

    public String getManagerName(String projectId) {
        ProjectTeam projectTeam = repository.findByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project team not found for projectId: " + projectId));

        return (projectTeam.getProjectManagerNames() != null && !projectTeam.getProjectManagerNames().isEmpty())
                ? projectTeam.getProjectManagerNames().get(0)
                : "Not Available";
    }

    public String getManagerEmail(String projectId) {
        ProjectTeam projectTeam = repository.findByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project team not found for projectId: " + projectId));

        return (projectTeam.getProjectManagerEmails() != null && !projectTeam.getProjectManagerEmails().isEmpty())
                ? projectTeam.getProjectManagerEmails().get(0)
                : "Not Available";
    }

    public ProjectTeam updateProjectTeamByProjectId(String projectId, ProjectTeamUpdateDTO dto) {
        ProjectTeam existingTeam = repository.findByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project team not found for projectId: " + projectId));

        projectTeamMapper.updateProjectTeamFromDto(dto, existingTeam);
        return repository.save(existingTeam);
    }

    public String deleteProjectTeamByProjectId(String projectId) {
        ProjectTeam existingTeam = repository.findByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project team not found for projectId: " + projectId));

        repository.delete(existingTeam);
        return "Project Team associated with projectId " + projectId + " deleted successfully.";
    }


//    public ProjectTeam updateManagerName(String projectId, String newName) {
//        ProjectTeam projectTeam = repository.findByProjectId(projectId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project team not found for projectId: " + projectId));
//
//        List<String> managerNames = projectTeam.getProjectManagerNames();
//        if (managerNames != null && !managerNames.isEmpty()) {
//            managerNames.set(0, newName);
//        } else {
//            managerNames = new ArrayList<>();
//            managerNames.add(newName);
//        }
//        projectTeam.setProjectManagerNames(managerNames);
//
//        return repository.save(projectTeam);
//    }
//
//    public ProjectTeam updateManagerEmail(String projectId, String newEmail) {
//        ProjectTeam projectTeam = repository.findByProjectId(projectId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project team not found for projectId: " + projectId));
//
//        List<String> managerEmails = projectTeam.getProjectManagerEmails();
//        if (managerEmails != null && !managerEmails.isEmpty()) {
//            managerEmails.set(0, newEmail);
//        } else {
//            managerEmails = new ArrayList<>();
//            managerEmails.add(newEmail);
//        }
//        projectTeam.setProjectManagerEmails(managerEmails);
//
//        return repository.save(projectTeam);
//    }

    // Update Manager Name
    public ProjectTeam updateManagerName(String projectId, String newName) {
        ProjectTeam team = repository.findByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Project team not found"));

        team.getProjectManagerNames().set(0, newName); // Update 0th index
        return repository.save(team);
    }

    // Update Manager Email
    public ProjectTeam updateManagerEmail(String projectId, String newEmail) {
        ProjectTeam team = repository.findByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Project team not found"));

        team.getProjectManagerEmails().set(0, newEmail); // Update 0th index
        return repository.save(team);
    }




}
