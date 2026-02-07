package com.smeproject.manage_project_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSMEDetailsDTO {
    private String projectId;
    private int numberOfSMEs;
    private String firstRequiredSkill;
}
