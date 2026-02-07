package com.smeportal.project_team_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smeportal.project_team_service.converter.JsonListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "ProjectTeam")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", nullable = false, unique = true)
    private Long teamId;

    @Column(name = "project_id", nullable = false, length = 10)
    private String projectId;

    @Convert(converter = JsonListConverter.class)
    @Column(name = "project_manager_names", columnDefinition = "JSON", nullable = true)
    private List<String> projectManagerNames;

    @Convert(converter = JsonListConverter.class)
    @Column(name = "project_manager_emails", columnDefinition = "JSON", nullable = true)
    private List<String> projectManagerEmails;

    @Convert(converter = JsonListConverter.class)
    @Column(name = "project_manager_phones", columnDefinition = "JSON", nullable = true)
    private List<String> projectManagerPhones;

    @Convert(converter = JsonListConverter.class)
    @Column(name = "sme_names", columnDefinition = "JSON", nullable = true)
    private List<String> smeNames;

    @Convert(converter = JsonListConverter.class)
    @Column(name = "sme_emails", columnDefinition = "JSON", nullable = true)
    private List<String> smeEmails;

    @Convert(converter = JsonListConverter.class)
    @Column(name = "sme_phones", columnDefinition = "JSON", nullable = true)
    private List<String> smePhones;

    @Column(name = "poc_name", nullable = true)
    private String pocName;

    @Column(name = "poc_email", nullable = true)
    private String pocEmail;

    @Column(name = "poc_phone", nullable = true)
    private String pocPhone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "created_at", nullable = true, updatable = false)
    private String createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
