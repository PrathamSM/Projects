package com.smeportal.sme_requirements_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class SMERequirementsUpdateDTO {
    private String projectId;
    private List<String> requiredSkills;
    private Integer numberOfSMEs;
    private String experienceLevel;
    private List<String> certificateName;
    private String domainExperienceRequired;
    private List<String> languageFluency;
}
