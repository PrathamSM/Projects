package com.smeportal.projects_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectDetailsDTO {

    private String projectId;
    private String projectName;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String projectContactPerson;
    private String projectLocation;
    private Double plannedBudget;

    public ProjectDetailsDTO(String projectId, String projectName, LocalDate projectStartDate,
                             LocalDate projectEndDate, String projectContactPerson, String projectLocation,
                             Double plannedBudget) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.projectContactPerson = projectContactPerson;
        this.projectLocation = projectLocation;
        this.plannedBudget = plannedBudget;
    }

}
