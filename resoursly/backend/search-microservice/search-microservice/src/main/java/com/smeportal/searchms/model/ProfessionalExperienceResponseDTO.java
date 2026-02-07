package com.smeportal.searchms.model;

import lombok.Data;

import java.util.List;

@Data
public class ProfessionalExperienceResponseDTO {
    private Long id;
    private Long profileId;
    private List<String> primaryDisciplines;
    private List<String> secondaryDisciplines;
    private String profession;
    private String qualification;
    private Float experienceYears;
    private Float relevantExperience;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public List<String> getPrimaryDisciplines() {
        return primaryDisciplines;
    }

    public void setPrimaryDisciplines(List<String> primaryDisciplines) {
        this.primaryDisciplines = primaryDisciplines;
    }

    public List<String> getSecondaryDisciplines() {
        return secondaryDisciplines;
    }

    public void setSecondaryDisciplines(List<String> secondaryDisciplines) {
        this.secondaryDisciplines = secondaryDisciplines;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Float getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Float experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Float getRelevantExperience() {
        return relevantExperience;
    }

    public void setRelevantExperience(Float relevantExperience) {
        this.relevantExperience = relevantExperience;
    }
}
