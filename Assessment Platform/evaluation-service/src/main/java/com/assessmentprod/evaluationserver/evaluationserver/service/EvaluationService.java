package com.assessmentprod.evaluationserver.evaluationserver.service;

import com.assessmentprod.evaluationserver.evaluationserver.dto.QuestionWithCorrectAns;
import com.assessmentprod.evaluationserver.evaluationserver.dto.SubmissionDTO;
import com.assessmentprod.evaluationserver.evaluationserver.dto.UpdateEvaluationDTO;
import com.assessmentprod.evaluationserver.evaluationserver.entity.Evaluation;
import com.assessmentprod.evaluationserver.evaluationserver.entity.Results;
import com.assessmentprod.evaluationserver.evaluationserver.exceptionHandler.EvaluationNotFoundException;
import com.assessmentprod.evaluationserver.evaluationserver.feignclient.QuestionClient;
import com.assessmentprod.evaluationserver.evaluationserver.repository.EvaluationRepository;
import com.assessmentprod.evaluationserver.evaluationserver.repository.ResultsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ResultsRepository resultsRepository;

    @Autowired
    private QuestionClient questionClient;

    /**
     * This method evaluates the submissions by comparing user answers with correct answers.
     * It listens to messages from RabbitMQ and processes the data.
     */
    @Transactional
    public void evaluateSubmissionFromQueue(SubmissionDTO submissionDTO) {
        Long submissionId = submissionDTO.getSubmission_id();
        Long assessmentId = submissionDTO.getAssessment_id();
        Long candidateId = submissionDTO.getCandidate_id();

        double totalScore = 0.0;

        // Get all test submissions from the SubmissionDTO
        List<SubmissionDTO.TestSubmission> tests = submissionDTO.getTests();

        // Collect all question IDs from all tests
        List<Long> allQuestionIds = tests.stream()
                .flatMap(test -> test.getAnswers().stream())
                .map(SubmissionDTO.Answer::getQuestion_id)
                .collect(Collectors.toList());



        // Fetch correct answers from Question microservice using Feign client
        List<QuestionWithCorrectAns> correctAnswers = questionClient
                .getQuestionsWithCorrectAns(allQuestionIds)
               .getBody();

        if(correctAnswers==null|| correctAnswers.isEmpty()){
            System.err.println("no correct answer retrieved from question Microservice ");
            return ;
        }

        // Create a map of question ID to correct answer for easy lookup
        Map<Long, Long> correctAnswerMap = correctAnswers.stream()
                .collect(Collectors.toMap(
                        QuestionWithCorrectAns::id,
                        QuestionWithCorrectAns::ansId));

        // Evaluate answers for each test
        Long testId = null;
        for (SubmissionDTO.TestSubmission testSubmission : tests) {
            double testScore = 0.0;
            testId = testSubmission.getTest_id();

            // Iterate over submitted answers and evaluate
            for (SubmissionDTO.Answer answer : testSubmission.getAnswers()) {
                Long questionId = answer.getQuestion_id();
                Long submittedOptionId = answer.getSubmittedOptionId();

                // Retrieve correct answer from the map

              Long correctOptionIds = correctAnswerMap.get(questionId);

                // Compare submitted answer with correct answer
                if (correctOptionIds != null && correctOptionIds.equals(submittedOptionId)) {
                    testScore += 1.0; // Increment score for correct answer}
}
            }

            totalScore += testScore; // Add to total score for this submission

            // Create and save Evaluation for each test
            Evaluation evaluation = new Evaluation();
            evaluation.setSubmissionId(submissionId);
            evaluation.setScore(testScore);
            evaluation.setComments("Test evaluation completed");
            evaluation.setAssessment_id(assessmentId);
            evaluation.setCandidatesid(candidateId);
            evaluation.setTestId(testId);
            evaluation.setCreatedAt(LocalDateTime.now());
            evaluation.setUpdatedAt(LocalDateTime.now());


            try {

                System.out.println("Saving evaluation: " + evaluation);

                evaluationRepository.save(evaluation);

                System.out.println("Evaluation saved successfully.");

            } catch (Exception e) {

                System.err.println("Error saving evaluation: " + e.getMessage());

                e.printStackTrace();

            }

        }

        Results results = new Results();
        results.setAssessmentId(assessmentId);
        results.setCandidateId(candidateId);

        results.setTotalScore(totalScore);
        results.setCreatedAt(LocalDateTime.now());

        System.out.println("Results Entity: " + results);
        try {
            System.out.println("Saving results: " + results);
            resultsRepository.save(results);
            System.out.println("Results saved successfully.");
        } catch (Exception e) {
            System.err.println("Error saving results: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get all evaluations
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    // Get evaluations by candidate ID
//    public List<Evaluation> getEvaluationsByCandidateId(Long candidateId) {
//        return evaluationRepository.findByCandidatesid(candidateId);
//    }
    public List<Evaluation> getEvaluationsByCandidateId(Long candidateId) {
        return evaluationRepository.findByCandidatesid(candidateId).stream()
                .collect(Collectors.toMap(
                        Evaluation::getAssessment_id,
                        eval -> eval,
                        (existing, replacement) -> existing.getUpdatedAt().isAfter(replacement.getUpdatedAt()) ? existing : replacement
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }
    // Get results by assessment ID
//    public List<Results> getResultsByAssessmentId(Long assessmentId) {
//        return resultsRepository.findByAssessmentId(assessmentId);
//    }
    public List<Results> getResultsByAssessmentId(Long assessmentId) {
        return resultsRepository.findByAssessmentId(assessmentId).stream()
                .collect(Collectors.groupingBy(Results::getCandidateId))
                .values()
                .stream()
                .map(list -> list.stream()
                        .max(Comparator.comparing(Results::getCreatedAt))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateEvaluation(Long evaluationId, UpdateEvaluationDTO updateEvaluationDTO) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new EvaluationNotFoundException("Evaluation with ID" + evaluationId + "not found"));

        evaluation.setComments(updateEvaluationDTO.getComments());
        evaluation.setUpdatedAt(LocalDateTime.now());

        evaluationRepository.save(evaluation);
        System.out.println("Evaluation updated successfully.");
    }

    // Delete evaluation
    @Transactional
    public void deleteEvaluation(Long evaluationId) {
        if(!evaluationRepository.existsById(evaluationId)){
            throw  new EvaluationNotFoundException("Evaluation With ID" + evaluationId + "does not exist");
        }
        evaluationRepository.deleteById(evaluationId);
        System.out.println("Evaluation deleted successfully.");
    }
}