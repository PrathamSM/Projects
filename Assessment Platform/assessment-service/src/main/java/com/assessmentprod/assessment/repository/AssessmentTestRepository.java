package com.assessmentprod.assessment.repository;

import com.assessmentprod.assessment.entity.AssessmentTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentTestRepository extends JpaRepository<AssessmentTest, Long> {

    List<AssessmentTest> findByAssessmentId(Long assessmentId);
}
