package com.smeproject.manage_project_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManageProjectDTO {
    private String projectId;
    private String projectName;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private Double plannedBudget;
    private String currency;
    private Integer numberOfSMEs;
    private String managerName;
    private String managerEmail;
    private Integer profileCount;
    private String firstRequiredSkill;
    private String projectContactPerson;
}
