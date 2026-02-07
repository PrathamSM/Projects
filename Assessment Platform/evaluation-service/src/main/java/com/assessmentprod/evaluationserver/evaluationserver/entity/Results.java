package com.assessmentprod.evaluationserver.evaluationserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Entity
public class Results {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long assessmentId;
    private Long candidateId;

    private double totalScore;
    private LocalDateTime createdAt;
}
