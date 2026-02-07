package com.assessmentprod.evaluationserver.evaluationserver.repository;

import java.util.List;

import com.assessmentprod.evaluationserver.evaluationserver.entity.Evaluation;
import com.assessmentprod.evaluationserver.evaluationserver.entity.Results;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResultsRepository extends JpaRepository<Results, Long> {
    List<Results> findByAssessmentId(Long assessmentId);

}
