package com.smeproject.manage_project_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManageProjectUpdateDTO {
    private String projectName;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private Double budget;
    private String currency;
    private Integer numberOfSMEsRequired;
    private String managerName;
    private String managerEmail;
}
