package com.assessmentprod.evaluationserver.evaluationserver;



import com.assessmentprod.evaluationserver.evaluationserver.controller.EvaluationController;
import com.assessmentprod.evaluationserver.evaluationserver.entity.Evaluation;
import com.assessmentprod.evaluationserver.evaluationserver.entity.Results;
import com.assessmentprod.evaluationserver.evaluationserver.exceptionHandler.AssessmentNotFoundException;
import com.assessmentprod.evaluationserver.evaluationserver.exceptionHandler.CandidateNotFoundException;
import com.assessmentprod.evaluationserver.evaluationserver.exceptionHandler.EvaluationNotFoundException;
import com.assessmentprod.evaluationserver.evaluationserver.service.EvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EvaluationControllerTests {

    @InjectMocks
    private EvaluationController evaluationController;

    @Mock
    private EvaluationService evaluationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEvaluations_ShouldReturnListOfEvaluations() {
        when(evaluationService.getAllEvaluations()).thenReturn(Collections.singletonList(new Evaluation()));
        ResponseEntity<List<Evaluation>> response = evaluationController.getAllEvaluations();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void deleteEvaluation_ShouldThrowExceptionWhenEvaluationNotFound() {
        Long evaluationId = 1L;
        doThrow(new EvaluationNotFoundException("Evaluation not found")).when(evaluationService).deleteEvaluation(evaluationId);

        assertThrows(EvaluationNotFoundException.class, () -> evaluationController.deleteEvaluation(evaluationId));
    }

    @Test
    void getResultsByAssessmentId_Found() {
        Long assessmentId = 1L;
        List<Results> results = Collections.singletonList(new Results());
        when(evaluationService.getResultsByAssessmentId(assessmentId)).thenReturn(results);

        ResponseEntity<List<Results>> response = evaluationController.getResultsByAssessmentId(assessmentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(results, response.getBody());
    }

    @Test
    void getResultsByAssessmentId_NotFound() {
        Long assessmentId = 1L;
        when(evaluationService.getResultsByAssessmentId(assessmentId)).thenReturn(Collections.emptyList());

        assertThrows(AssessmentNotFoundException.class, () -> evaluationController.getResultsByAssessmentId(assessmentId));
    }

    @Test
    void getScoresByCandidateId_Found() {
        Long candidateId = 1L;
        List<Evaluation> evaluations = Collections.singletonList(new Evaluation());
        when(evaluationService.getEvaluationsByCandidateId(candidateId)).thenReturn(evaluations);

        ResponseEntity<List<Evaluation>> response = evaluationController.getScoresByCandidateId(candidateId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(evaluations, response.getBody());
    }

    @Test
    void getScoresByCandidateId_NotFound() {
        Long candidateId = 1L;
        when(evaluationService.getEvaluationsByCandidateId(candidateId)).thenReturn(Collections.emptyList());

        assertThrows(CandidateNotFoundException.class, () -> evaluationController.getScoresByCandidateId(candidateId));
    }
}
