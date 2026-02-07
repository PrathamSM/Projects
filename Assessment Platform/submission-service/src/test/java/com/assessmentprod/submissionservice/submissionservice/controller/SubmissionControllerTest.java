package com.assessmentprod.submissionservice.submissionservice.controller;



import com.assessmentprod.submissionservice.submissionservice.dto.SubmissionDTO;
import com.assessmentprod.submissionservice.submissionservice.entity.Submission_entity;
import com.assessmentprod.submissionservice.submissionservice.exception.SubmissionNotFoundException;
import com.assessmentprod.submissionservice.submissionservice.service.SubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubmissionControllerTest {

    @Mock
    private SubmissionService submissionService;

    @InjectMocks
    private SubmissionController submissionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSubmission() {
        SubmissionDTO submissionDTO = new SubmissionDTO();
        List<Submission_entity> submissions = new ArrayList<>();
        submissions.add(new Submission_entity());

        when(submissionService.createSubmission(any(SubmissionDTO.class))).thenReturn(submissions);

        ResponseEntity<String> response = submissionController.createSubmission(submissionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("submission created sucessfully .", response.getBody());
    }

    @Test
    void getAllSubmissions() {
        List<Submission_entity> submissions = new ArrayList<>();
        submissions.add(new Submission_entity());

        when(submissionService.getAllSubmissions()).thenReturn(submissions);

        ResponseEntity<List<Submission_entity>> response = submissionController.getAllSubmissions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(submissions, response.getBody());
    }

    @Test
    void getSubmissionById() {
        Submission_entity submission = new Submission_entity();
        submission.setId(1L);

        when(submissionService.getSubmissionById(1L)).thenReturn(submission);

        ResponseEntity<Object> response = submissionController.getSubmissionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(submission, response.getBody());
    }

    @Test
    void getSubmissionById_ShouldReturnNotFound() {
        when(submissionService.getSubmissionById(1L)).thenThrow(new SubmissionNotFoundException("Submission not found"));

        ResponseEntity<Object> response = submissionController.getSubmissionById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Submission with ID 1 not found.", response.getBody());
    }

    @Test
    void deleteSubmission() {
        doNothing().when(submissionService).deleteSubmission(1L);

        ResponseEntity<String> response = submissionController.deleteSubmission(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}