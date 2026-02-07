package com.assessmentprod.submissionservice.submissionservice.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.assessmentprod.submissionservice.submissionservice.dto.SubmissionDTO;
import com.assessmentprod.submissionservice.submissionservice.entity.Submission_entity;
import com.assessmentprod.submissionservice.submissionservice.exception.SubmissionNotFoundException;
import com.assessmentprod.submissionservice.submissionservice.exception.SubmissionProcessingException;
import com.assessmentprod.submissionservice.submissionservice.queues.RabbitMQProducer;
import com.assessmentprod.submissionservice.submissionservice.repository.SubmissionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final ObjectMapper objectMapper;
    private final RabbitMQProducer rabbitMQProducer;

    public SubmissionService(SubmissionRepository submissionRepository, ObjectMapper objectMapper, RabbitMQProducer rabbitMQProducer) {
        this.submissionRepository = submissionRepository;
        this.objectMapper = objectMapper;
        this.rabbitMQProducer = rabbitMQProducer;
    }


    public List<Submission_entity> createSubmission(SubmissionDTO submissionDTO)
    {
        try{
            List<Submission_entity> submissions = new ArrayList<>();
            for (SubmissionDTO.TestSubmission test : submissionDTO.getTests()) {
                String answersJson = objectMapper.writeValueAsString(test.getAnswers());
                Submission_entity submission = new Submission_entity();
                submission.setTest_id(test.getTest_id());
                submission.setCandidate_id(submissionDTO.getCandidate_id());
                submission.setAssessment_id(submissionDTO.getAssessment_id());
                submission.setSubmittedAt(LocalDateTime.now());
                submission.setAnswers(answersJson);
                Submission_entity savedSubmission = submissionRepository.save(submission);
                submissions.add(savedSubmission);
            }
            submissionDTO.setSubmission_id(submissions.get(0).getId());
            submissionDTO.setSubmittedAt(LocalDateTime.now());
            rabbitMQProducer.sendMessage(submissionDTO);
            return submissions;
        }
        catch (JsonProcessingException e)
        {
            throw new SubmissionProcessingException("Failed to process the submission data", e);
        }
        catch (Exception e) {
            throw new SubmissionProcessingException("An unexpected error occurred during submission creation , provide correct pay load", e);
        }
    }

    public List<Submission_entity> updateSubmission(Long id, SubmissionDTO updatedSubmissionDTO) {
        if (!submissionRepository.existsById(id)) {
            throw new SubmissionNotFoundException("Submission with ID " + id + " not found");
        }

        List<Submission_entity> submissions = new ArrayList<>();

        for (SubmissionDTO.TestSubmission test : updatedSubmissionDTO.getTests()) {
            Submission_entity submission = submissionRepository.findById(id).get();
            // Do not set the ID here, so a new record is created for each test
            submission.setTest_id(Long.valueOf(String.valueOf(test.getTest_id()))); // Set individual test_id
            submission.setCandidate_id(updatedSubmissionDTO.getCandidate_id());
            submission.setAssessment_id(updatedSubmissionDTO.getAssessment_id());
            submission.setSubmittedAt(LocalDateTime.now());

            // Convert answers to JSON format
            try {
                // Convert answers to JSON format
                submission.setAnswers(objectMapper.writeValueAsString(test.getAnswers()));
            } catch (JsonProcessingException e) {
                throw new SubmissionProcessingException("Failed to process answers for submission", e);
            }

            // Save each new submission as a unique entry
            Submission_entity savedSubmission = submissionRepository.save(submission);
            submissions.add(savedSubmission);
        // updatedSubmissionDTO.setSubmission_id(submissions.get(0).getId());
        }

     // rabbitMQProducer.sendMessage(updatedSubmissionDTO);

        return submissions;
    }

    public List<Submission_entity> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Submission_entity getSubmissionById(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new SubmissionNotFoundException("Submission with ID " + id + " not found."));
    }

    public void deleteSubmission(Long id) {
if(!submissionRepository.existsById(id)){
    throw new SubmissionNotFoundException("submission with ID " + id + "does not exist");
}
        submissionRepository.deleteById(id);
    }
}