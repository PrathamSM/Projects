package com.smeproject.manage_project_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectDetailsDTO {
    private String projectId;
    private String projectName;
    private String projectStartDate;
    private String projectEndDate;
    private String projectContactPerson;
    private String projectLocation;
    private Double plannedBudget;
}
