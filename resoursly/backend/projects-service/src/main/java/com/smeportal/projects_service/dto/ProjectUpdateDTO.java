package com.smeportal.projects_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProjectUpdateDTO {
    private String projectName;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String projectContactPerson;
    private String projectContactEmail;
    private String projectContactPhone;
    private String projectLocation;
    private String projectDescription;
    private BigDecimal plannedBudget;
    private String billingModel;
    private BigDecimal budgetAlertThreshold;
    private String currency;


}
