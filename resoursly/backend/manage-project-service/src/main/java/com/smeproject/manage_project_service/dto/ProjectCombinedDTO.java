package com.smeproject.manage_project_service.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCombinedDTO {
    private String projectId;
    private String projectName;
    private String projectStartDate;
    private String projectEndDate;
    private String projectContactPerson;
    private String projectLocation;
    private Double plannedBudget;
    private Integer numberOfSMEs;
    private String firstRequiredSkill;
    private Integer profileCount;
}
