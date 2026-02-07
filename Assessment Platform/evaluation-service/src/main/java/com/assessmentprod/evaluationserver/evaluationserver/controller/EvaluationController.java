package com.assessmentprod.evaluationserver.evaluationserver.controller;

import com.assessmentprod.evaluationserver.evaluationserver.dto.UpdateEvaluationDTO;
import com.assessmentprod.evaluationserver.evaluationserver.entity.Evaluation;
import com.assessmentprod.evaluationserver.evaluationserver.entity.Results;
import com.assessmentprod.evaluationserver.evaluationserver.exceptionHandler.AssessmentNotFoundException;
import com.assessmentprod.evaluationserver.evaluationserver.exceptionHandler.CandidateNotFoundException;
import com.assessmentprod.evaluationserver.evaluationserver.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;




@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    // Get all evaluations
    @GetMapping
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        List<Evaluation> evaluations = evaluationService.getAllEvaluations();
        return ResponseEntity.ok(evaluations);
    }

    // Get scores by candidate ID
    @GetMapping("/candidates/{candidates_id}/scores")
    public ResponseEntity<List<Evaluation>> getScoresByCandidateId(@PathVariable Long candidates_id) {
        List<Evaluation> evaluations = evaluationService.getEvaluationsByCandidateId(candidates_id);
       if(evaluations.isEmpty()){
           throw new CandidateNotFoundException("candidate with ID" + candidates_id + " not found ");
       }
        return ResponseEntity.ok(evaluations);
    }

    // Get results by assessment ID
    @GetMapping("/assessments/{assessmentId}/results")
    public ResponseEntity<List<Results>> getResultsByAssessmentId(@PathVariable Long assessmentId) {
        List<Results> results = evaluationService.getResultsByAssessmentId(assessmentId);
        if(results.isEmpty()){
            throw new AssessmentNotFoundException("Assessment with ID " + assessmentId + "not found");
        }
        return ResponseEntity.ok(results);
    }
    @DeleteMapping("/{evaluationId}")
    public ResponseEntity<String>deleteEvaluation(@PathVariable Long evaluationId){
        evaluationService.deleteEvaluation(evaluationId);
        return ResponseEntity.ok("deleted evaluation Id"+  evaluationId + "successfully ");
    }

    public ResponseEntity<String>updateEvaluation(@PathVariable Long evaluationId , @RequestBody UpdateEvaluationDTO updateEvaluationDTO){
        evaluationService.updateEvaluation(evaluationId,updateEvaluationDTO);
        return  ResponseEntity.ok("update evaluation ID" + evaluationId + "succefully");
    }

}
