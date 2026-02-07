package com.assessmentprod.submissionservice.submissionservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "submissions")
public class Submission_entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_id", nullable = false) // Adjusted naming to snake_case
    private Long test_id;

    @Column(name = "candidate_id", nullable = false) // Adjusted naming
    private Long candidate_id;

    @Column(name = "assessment_id", nullable = false) // Adjusted naming
    private Long assessment_id;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
    
    @Lob
    @Column(name = "answers", nullable = false, columnDefinition = "LONGTEXT")
    private String answers; // Keep this if you want to store JSON directly


}