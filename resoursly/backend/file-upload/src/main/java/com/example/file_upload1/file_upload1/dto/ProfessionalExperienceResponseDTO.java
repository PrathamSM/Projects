package com.example.file_upload1.file_upload1.dto;

import java.util.List;


public class ProfessionalExperienceResponseDTO {
    private Long id;
    private Long profileId;
    private List<String> primaryDisciplines; // Discipline names
    private List<String> secondaryDisciplines;
    private String profession;
    private String qualification;
    private Float experienceYears;
    private Float relevantExperience;
}