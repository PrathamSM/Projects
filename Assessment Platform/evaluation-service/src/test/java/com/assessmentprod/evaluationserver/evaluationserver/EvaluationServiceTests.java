package com.assessmentprod.evaluationserver.evaluationserver;

import com.assessmentprod.evaluationserver.evaluationserver.dto.SubmissionDTO;
import com.assessmentprod.evaluationserver.evaluationserver.dto.UpdateEvaluationDTO;
import com.assessmentprod.evaluationserver.evaluationserver.entity.Evaluation;
import com.assessmentprod.evaluationserver.evaluationserver.entity.Results;
import com.assessmentprod.evaluationserver.evaluationserver.exceptionHandler.EvaluationNotFoundException;
import com.assessmentprod.evaluationserver.evaluationserver.repository.EvaluationRepository;
import com.assessmentprod.evaluationserver.evaluationserver.repository.ResultsRepository;
import com.assessmentprod.evaluationserver.evaluationserver.service.EvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EvaluationServiceTests {

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private ResultsRepository resultsRepository;

    @Mock
    private SubmissionDTO submissionDTO;

    @Mock
    private UpdateEvaluationDTO updateEvaluationDTO;

    @InjectMocks
    private EvaluationService evaluationService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
    }



    @Test
    void getAllEvaluations() {
        // Test getAllEvaluations method
        // Arrange
        List<Evaluation> evaluations = new ArrayList<>();
        when(evaluationRepository.findAll()).thenReturn(evaluations);

        // Act
        List<Evaluation> result = evaluationService.getAllEvaluations();

        // Assert
        assertEquals(evaluations, result);
    }

    @Test
    void getEvaluationsByCandidateId() {
        // Test getEvaluationsByCandidateId method
        // Arrange
        List<Evaluation> evaluations = new ArrayList<>();
        when(evaluationRepository.findByCandidatesid(1L)).thenReturn(evaluations);

        // Act
        List<Evaluation> result = evaluationService.getEvaluationsByCandidateId(1L);

        // Assert
        assertEquals(evaluations, result);
    }

    @Test
    void getResultsByAssessmentId() {
        // Test getResultsByAssessmentId method
        // Arrange
        List<Results> results = new ArrayList<>();
        when(resultsRepository.findByAssessmentId(1L)).thenReturn(results);

        // Act
        List<Results> result = evaluationService.getResultsByAssessmentId(1L);

        // Assert
        assertEquals(results, result);
    }

    @Test
    void updateEvaluation() {
        // Test updateEvaluation method
        // Arrange
        Evaluation evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setComments("Test comments");
        evaluation.setUpdatedAt(LocalDateTime.now());
        when(evaluationRepository.findById(1L)).thenReturn(java.util.Optional.of(evaluation));
        when(updateEvaluationDTO.getComments()).thenReturn("Updated comments");

        // Act
        evaluationService.updateEvaluation(1L, updateEvaluationDTO);

        // Assert
        verify(evaluationRepository, times(1)).save(any(Evaluation.class));
    }

    @Test
    void updateEvaluation_NotFound() {
        // Test updateEvaluation method with not found evaluation
        // Arrange
        when(evaluationRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act and Assert
        assertThrows(EvaluationNotFoundException.class, () -> evaluationService.updateEvaluation(1L, updateEvaluationDTO));
    }

    @Test
    void deleteEvaluation() {
        // Test deleteEvaluation method
        // Arrange
        when(evaluationRepository.existsById(1L)).thenReturn(true);

        // Act
        evaluationService.deleteEvaluation(1L);

        // Assert
        verify(evaluationRepository, times(1)).deleteById(1L);
    }

}