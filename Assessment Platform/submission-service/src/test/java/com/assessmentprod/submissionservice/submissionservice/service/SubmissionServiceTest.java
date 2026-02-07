package com.assessmentprod.submissionservice.submissionservice.service;

import com.assessmentprod.submissionservice.submissionservice.dto.SubmissionDTO;
import com.assessmentprod.submissionservice.submissionservice.entity.Submission_entity;
import com.assessmentprod.submissionservice.submissionservice.exception.SubmissionNotFoundException;
import com.assessmentprod.submissionservice.submissionservice.queues.RabbitMQProducer;
import com.assessmentprod.submissionservice.submissionservice.repository.SubmissionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SubmissionService submissionService;

    private RabbitMQProducer rabbitMQProducer;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void createSubmission() throws JsonProcessingException {
//        SubmissionDTO submissionDTO = new SubmissionDTO();
//        submissionDTO.setCandidate_id(1L);
//        submissionDTO.setAssessment_id(1L);
//        SubmissionDTO.TestSubmission testSubmission = new SubmissionDTO.TestSubmission();
//        testSubmission.setTest_id(1L);
//        List<SubmissionDTO.TestSubmission> tests = new ArrayList<>();
//        tests.add(testSubmission);
//        submissionDTO.setTests(tests);
//
//        Submission_entity savedSubmission = new Submission_entity();
//        savedSubmission.setId(1L);
//
//
//        when(objectMapper.writeValueAsString(any())).thenReturn("jsonString");
//        when(submissionRepository.save(any(Submission_entity.class))).thenReturn(savedSubmission);
//
//        List<Submission_entity> submissions = submissionService.createSubmission(submissionDTO);
//
//        assertNotNull(submissions);
//        assertEquals(1, submissions.size());
//        verify(rabbitMQProducer,times(1)).sendMessage(submissionDTO);
//    }

    @Test
    void updateSubmission() throws JsonProcessingException {
        SubmissionDTO submissionDTO = new SubmissionDTO();
        submissionDTO.setCandidate_id(1L);
        submissionDTO.setAssessment_id(1L);

        SubmissionDTO.TestSubmission testSubmission = new SubmissionDTO.TestSubmission();
        testSubmission.setTest_id(1L);
        List<SubmissionDTO.TestSubmission> tests = new ArrayList<>();
        tests.add(testSubmission);
        submissionDTO.setTests(tests);

        Submission_entity existingSubmission = new Submission_entity();
        existingSubmission.setId(1L);

        when(submissionRepository.existsById(anyLong())).thenReturn(true);
        when(submissionRepository.findById(anyLong())).thenReturn(Optional.of(existingSubmission));
        when(objectMapper.writeValueAsString(any())).thenReturn("jsonString");
        when(submissionRepository.save(any(Submission_entity.class))).thenReturn(existingSubmission);

        List<Submission_entity> updatedSubmissions = submissionService.updateSubmission(1L, submissionDTO);

        assertNotNull(updatedSubmissions);
        assertEquals(1, updatedSubmissions.size());
    }

    @Test
    void getAllSubmissions() {
        List<Submission_entity> submissions = new ArrayList<>();
        submissions.add(new Submission_entity());

        when(submissionRepository.findAll()).thenReturn(submissions);

        List<Submission_entity> result = submissionService.getAllSubmissions();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getSubmissionById() {
        Submission_entity submission = new Submission_entity();
        submission.setId(1L);

        when(submissionRepository.findById(anyLong())).thenReturn(Optional.of(submission));

        Submission_entity result = submissionService.getSubmissionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getSubmissionById_ThrowSubmissionNotFoundException() {
        when(submissionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(SubmissionNotFoundException.class, () -> submissionService.getSubmissionById(1L));
    }

    @Test
    void deleteSubmission() {
        when(submissionRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(submissionRepository).deleteById(anyLong());

        submissionService.deleteSubmission(1L);

        verify(submissionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteSubmission_ShouldThrowSubmissionNotFoundException() {
        when(submissionRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(SubmissionNotFoundException.class, () -> submissionService.deleteSubmission(1L));
    }
}