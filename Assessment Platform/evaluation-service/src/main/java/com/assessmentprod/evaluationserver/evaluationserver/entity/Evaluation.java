package com.assessmentprod.evaluationserver.evaluationserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@Entity
public class Evaluation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long submissionId;

	private double score;
	private String comments;
	private Long testId;
	private Long assessment_id;
	private Long candidatesid;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}