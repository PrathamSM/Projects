package com.assessmentprod.submissionservice.submissionservice.controller;

import java.util.List;


import com.assessmentprod.submissionservice.submissionservice.entity.Submission_entity;
import com.assessmentprod.submissionservice.submissionservice.exception.SubmissionNotFoundException;
import com.assessmentprod.submissionservice.submissionservice.exception.SubmissionProcessingException;
import com.assessmentprod.submissionservice.submissionservice.queues.RabbitMQProducer;
import com.assessmentprod.submissionservice.submissionservice.service.SubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.assessmentprod.submissionservice.submissionservice.dto.SubmissionDTO;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {
    private static final Logger log = LoggerFactory.getLogger(SubmissionController.class);
    private final SubmissionService submissionService;
    private final RabbitMQProducer rabbitMQProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public SubmissionController(SubmissionService submissionService, RabbitMQProducer rabbitMQProducer, ObjectMapper objectMapper) {
        this.submissionService = submissionService;
        this.rabbitMQProducer = rabbitMQProducer;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/submit") // Just for adding the data to the db
    public ResponseEntity<String> createSubmission(@RequestBody SubmissionDTO submissionDTO) {
        try {
            List<Submission_entity> submission = submissionService.createSubmission(submissionDTO);
            return new ResponseEntity<>("submission created sucessfully .", HttpStatus.CREATED); // Return created submission
        } catch (SubmissionProcessingException e) {
            log.error("Error processing submission", e);
            return new ResponseEntity<>("failed to create submission " + e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Submission_entity>> getAllSubmissions() {
        List<Submission_entity> submissions = submissionService.getAllSubmissions();
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSubmissionById(@PathVariable Long id) {
        try {
            Submission_entity submission = submissionService.getSubmissionById(id);
            return new ResponseEntity<>(submission, HttpStatus.OK);
        } catch (SubmissionNotFoundException e) {
            log.error("Submission not found: {}", e.getMessage(), e);
            return new ResponseEntity<>("Submission with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error while fetching submission", e);
            return new ResponseEntity<>("An unexpected error occurred while fetching the submission.", HttpStatus.INTERNAL_SERVER_ERROR);
}
    }
    @PutMapping("/{id}")
    public ResponseEntity<List<Submission_entity>> updateSubmission(@PathVariable Long id, @RequestBody SubmissionDTO updatedSubmissionDTO) {
        try {
            List <Submission_entity> updatedSubmission = submissionService.updateSubmission(id, updatedSubmissionDTO);
            if (updatedSubmission != null) {
                return new ResponseEntity<>(updatedSubmission, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (SubmissionProcessingException e) {
            log.error("Error processing submission update", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubmission(@PathVariable Long id) {
        try {
            submissionService.deleteSubmission(id);
            return new ResponseEntity<>("Submission deleted successfully.", HttpStatus.NO_CONTENT);
        } catch (SubmissionNotFoundException e) {
            log.error("Error deleting submission", e);
            return new ResponseEntity<>("Submission with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error during deletion", e);
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}