package com.smeportal.projects_service.service;

import com.smeportal.projects_service.dto.ProjectDetailsDTO;
import com.smeportal.projects_service.dto.ProjectIdNameDTO;
import com.smeportal.projects_service.dto.ProjectUpdateDTO;
import com.smeportal.projects_service.entity.Projects;
import com.smeportal.projects_service.exception.ProjectNotFoundException;
import com.smeportal.projects_service.repository.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ProjectsServiceImpl implements ProjectsService {

    @Autowired
    private ProjectsRepository projectsRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    @Override
    public Projects createProject(Projects project) {
        String lastProjectId = projectsRepository.findLastProjectId();

        if (lastProjectId == null || lastProjectId.isEmpty()) {
            if (!"PR000001".equals(project.getProjectId())) {
                throw new RuntimeException("First project ID must be PR000001.");
            }
        } else {
            String expectedNextId = generateProjectId(lastProjectId);
            if (!project.getProjectId().equals(expectedNextId)) {
                throw new RuntimeException("Enter " + expectedNextId + " as the next project ID.");
            }
        }

        // Convert String date format to LocalDate
        project.setProjectStartDate(LocalDate.parse(project.getProjectStartDate().format(FORMATTER), FORMATTER));
        project.setProjectEndDate(LocalDate.parse(project.getProjectEndDate().format(FORMATTER), FORMATTER));

        return projectsRepository.save(project);
    }





private String generateProjectId(String lastProjectId) {
        int lastIdNumber = Integer.parseInt(lastProjectId.substring(2));
        int newIdNumber = lastIdNumber + 1;
        return String.format("PR%06d", newIdNumber);
    }

    @Override
    public Projects getProjectById(String projectId) {
        return projectsRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
    }

    @Override
    public List<Projects> getAllProjects() {
        return projectsRepository.findAll();
    }


    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public Projects updateProject(String projectId, ProjectUpdateDTO projectDetails) {
        Projects existingProject = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " not found."));

        // Map non-null fields from DTO to entity
        projectMapper.updateProjectFromDto(projectDetails, existingProject);

        return projectsRepository.save(existingProject);
    }


    @Override
    public void deleteProject(String projectId) {
        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        projectsRepository.delete(project);
    }




    @Override
    public String getNextProjectId() {
        String lastProjectId = projectsRepository.findLastProjectId();

        // If no project exists, return the first ID
        if (lastProjectId == null || lastProjectId.isEmpty()) {
            return "PR000001";
        }

        return generateProjectId(lastProjectId);
    }


    public boolean existsByProjectId(String projectId) {
        return projectsRepository.existsByProjectId(projectId);
    }

//end of    //26 02

    @Override
    public String getProjectNameById(String projectId) {
        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
        return project.getProjectName();
    }

    @Override
    public LocalDate getProjectStartDateById(String projectId) {
        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
        return project.getProjectStartDate();
    }

    @Override
    public LocalDate getProjectEndDateById(String projectId) {
        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
        return project.getProjectEndDate();
    }


    @Override
    public String getCurrencyByProjectId(String projectId) {
        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        return project.getCurrency() != null ? project.getCurrency() : "Not Available";
    }

    @Override
    public Double getPlannedBudgetByProjectId(String projectId) {
        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
        return project.getPlannedBudget();
    }

    @Override
    public Projects updateProjectById(String projectId, ProjectUpdateDTO projectDetails) {
        Projects existingProject = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        projectMapper.updateProjectFromDto(projectDetails, existingProject);

        return projectsRepository.save(existingProject);
    }

    //04 03

    @Override
    public List<ProjectIdNameDTO> getAllProjectIdsAndNames() {
        return projectsRepository.findAllProjectIdsAndNames();
    }

    @Override
    public String getProjectContactPersonById(String projectId) {
        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
        return project.getProjectContactPerson();
    }

    @Override
    public String getProjectContactEmailById(String projectId) {
        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
        return project.getProjectContactEmail();
    }

    @Override
    public String getProjectContactPhoneById(String projectId) {
        Projects project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
        return project.getProjectContactPhone();
    }

    @Override
    public List<ProjectDetailsDTO> getAllProjectDetails() {
        List<Projects> projectsList = projectsRepository.findAll();

        return projectsList.stream()
                .map(project -> new ProjectDetailsDTO(
                        project.getProjectId(),
                        project.getProjectName(),
                        project.getProjectStartDate(),
                        project.getProjectEndDate(),
                        project.getProjectContactPerson(),
                        project.getProjectLocation(),
                        project.getPlannedBudget()
                ))
                .collect(Collectors.toList());
    }

    public Long getProjectCount() {
        return projectsRepository.count();
    }



}

