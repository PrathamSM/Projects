package com.smeportal.projects_service.service;

import com.smeportal.projects_service.dto.ProjectDetailsDTO;
import com.smeportal.projects_service.dto.ProjectIdNameDTO;
import com.smeportal.projects_service.dto.ProjectUpdateDTO;
import com.smeportal.projects_service.entity.Projects;

import java.time.LocalDate;
import java.util.List;

//the interface for the projects service is used for creating, retrieving, updating and deleting projects from the implementation class
//the main purpose of the interface is to decouple the controller from the implementation class

public interface ProjectsService {
    Projects createProject(Projects project);
    Projects getProjectById(String projectId);
    List<Projects> getAllProjects();
    //Projects updateProject(String projectId, Projects project);
    Projects updateProject(String projectId, ProjectUpdateDTO projectDetails); //now we can update the feilds according to requirements
    void deleteProject(String projectId);
    public Long getProjectCount() ;

    String getNextProjectId();
    String getProjectNameById(String projectId);

    LocalDate getProjectStartDateById(String projectId);
    LocalDate getProjectEndDateById(String projectId);

    String getCurrencyByProjectId(String projectId);
    Double getPlannedBudgetByProjectId(String projectId);

    boolean existsByProjectId(String projectId);

    Projects updateProjectById(String projectId, ProjectUpdateDTO projectDetails);

    List<ProjectIdNameDTO> getAllProjectIdsAndNames();

    //06-03

    String getProjectContactPersonById(String projectId);
    String getProjectContactEmailById(String projectId);
    String getProjectContactPhoneById(String projectId);

    //07-03
    List<ProjectDetailsDTO> getAllProjectDetails();

}
