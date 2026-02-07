package com.assessmentprod.evaluationserver.evaluationserver.repository;

import java.util.List;
import com.assessmentprod.evaluationserver.evaluationserver.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

	 List<Evaluation> findByCandidatesid(Long candidates_id);


}