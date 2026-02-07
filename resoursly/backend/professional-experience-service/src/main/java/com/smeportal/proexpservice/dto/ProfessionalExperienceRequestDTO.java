package com.smeportal.proexpservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfessionalExperienceRequestDTO {
    private Long profileId;
    private List<String> primaryDisciplines; // Discipline names
    private List<String> secondaryDisciplines;
    private String profession;
    private String qualification;
    private Float experienceYears;
    private Float relevantExperience;
}