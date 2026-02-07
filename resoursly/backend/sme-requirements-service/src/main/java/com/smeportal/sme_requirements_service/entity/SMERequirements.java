package com.smeportal.sme_requirements_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smeportal.sme_requirements_service.converter.CertificateJSONConverter;
import com.smeportal.sme_requirements_service.converter.LanguageFluencyJSONConverter;
import com.smeportal.sme_requirements_service.converter.SkillsJSONConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "SMERequirements")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SMERequirements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requirement_id", nullable = false, unique = true)
    private Long requirementId;  // Primary Key

    @Column(name = "project_id", nullable = false, length = 10)
    private String projectId;  // Foreign Key referencing Projects.project_id

//    @Column(name = "required_skills", nullable = false)
//    private String requiredSkills;

    @Convert(converter = SkillsJSONConverter.class)
    @Column(name = "required_skills", columnDefinition = "JSON", nullable = false)
    private List<String> requiredSkills;


    @Column(name = "number_of_smes", nullable = false)
    private Integer numberOfSMEs;

    @Column(name = "experience_level", nullable = false)
    private String experienceLevel;

//    @Column(name = "certifications_required", nullable = false)
//    private Boolean certificationsRequired;

    @Convert(converter = CertificateJSONConverter.class)
    @Column(name = "certifications_required", columnDefinition = "JSON", nullable = true)
    private List<String> certificateName;

    @Column(name = "domain_experience_required", nullable = false)
    //private Boolean domainExperienceRequired;
    private String domainExperienceRequired;

//    @Column(name = "language_fluency", nullable = false)
//    private String languageFluency;

    @Convert(converter = LanguageFluencyJSONConverter.class)
    @Column(name = "language_fluency", columnDefinition = "JSON", nullable = false)
    private List<String> languageFluency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

}
