package com.example.file_upload1.file_upload1.dto;

import java.util.List;


public class ProfessionalExperienceRequestDTO {
    private Long profileId;
    private List<String> primaryDisciplines; // Discipline names
    private List<String> secondaryDisciplines;
    private String profession;
    private String qualification;
    private Float experienceYears;
    private Float relevantExperience;

    // Getter and Setter for profileId
    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    // Getter and Setter for primaryDisciplines
    public List<String> getPrimaryDisciplines() {
        return primaryDisciplines;
    }

    public void setPrimaryDisciplines(List<String> primaryDisciplines) {
        this.primaryDisciplines = primaryDisciplines;
    }

    // Getter and Setter for secondaryDisciplines
    public List<String> getSecondaryDisciplines() {
        return secondaryDisciplines;
    }

    public void setSecondaryDisciplines(List<String> secondaryDisciplines) {
        this.secondaryDisciplines = secondaryDisciplines;
    }

    // Getter and Setter for profession
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    // Getter and Setter for qualification
    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    // Getter and Setter for experienceYears
    public Float getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Float experienceYears) {
        this.experienceYears = experienceYears;
    }

    // Getter and Setter for relevantExperience
    public Float getRelevantExperience() {
        return relevantExperience;
    }

    public void setRelevantExperience(Float relevantExperience) {
        this.relevantExperience = relevantExperience;
    }
}