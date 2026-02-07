package com.smeportal.proexpservice.model;

import com.smeportal.proexpservice.converter.JsonArrayConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ProfessionalExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long profileId;

    @Convert(converter = JsonArrayConverter.class) // Converts List<Long> to JSON
    private List<Long> primaryDisciplineIds; // Discipline IDs from Discipline Service

    @Convert(converter = JsonArrayConverter.class)
    private List<Long> secondaryDisciplineIds;

    //@Pattern(regexp = "^[A-Z]+(?: [A-Z]+)*$", message = "Profession must be in uppercase and words must be separated by a space")
    private String profession;
    private String qualification;
    private Float experienceYears;
    private Float relevantExperience;

    // Getters and Setters
}
