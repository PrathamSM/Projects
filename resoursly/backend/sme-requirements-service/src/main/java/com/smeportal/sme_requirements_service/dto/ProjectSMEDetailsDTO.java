package com.smeportal.sme_requirements_service.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSMEDetailsDTO {
    private String projectId;
    private Integer numberOfSMEs;
    private String firstRequiredSkill;


}
