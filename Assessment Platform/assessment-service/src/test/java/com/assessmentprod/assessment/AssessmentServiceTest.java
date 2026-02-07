package com.assessmentprod.assessment;


import com.assessmentprod.assessment.dto.AssessmentReq;
import com.assessmentprod.assessment.dto.AssessmentResDto;
import com.assessmentprod.assessment.dto.AssessmentWithTestIdsRes;
import com.assessmentprod.assessment.dto.test.TestResDto;
import com.assessmentprod.assessment.entity.Assessment;
import com.assessmentprod.assessment.entity.AssessmentTest;
import com.assessmentprod.assessment.exception.BadRequestException;
import com.assessmentprod.assessment.repository.AssessmentRepository;
import com.assessmentprod.assessment.repository.AssessmentTestRepository;
import com.assessmentprod.assessment.service.AssessmentService;
import com.assessmentprod.assessment.service.client.TestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    @InjectMocks
    private AssessmentService assessmentService;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private AssessmentTestRepository assessmentTestRepository;

    @Mock
    private TestClient testClient;

    private Assessment assessment;
    private AssessmentReq assessmentReq;

    @BeforeEach
    void setUp() {
        assessment = new Assessment();
        assessment.setId(1L);
        assessment.setName("Java Assessment");

        assessmentReq = new AssessmentReq("Java Assessment", List.of(1L, 2L));
    }

    @Test
    void addAssessment_Success() {
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);
        when(assessmentTestRepository.saveAll(anyList())).thenReturn(List.of(new AssessmentTest(), new AssessmentTest()));

        AssessmentWithTestIdsRes result = assessmentService.addAssessment(assessmentReq);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Java Assessment", result.name());
        assertEquals(List.of(1L, 2L), result.testIds());
    }

    @Test
    void addAssessment_NameMissing_ShouldThrowException() {
        AssessmentReq invalidReq = new AssessmentReq(null, List.of(1L, 2L));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            assessmentService.addAssessment(invalidReq);
        });

        assertEquals("Name is required.", exception.getMessage());
    }

    @Test
    void getAssessmentById_Success() {
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));
        when(testClient.getByMultipleTestIds(anyList())).thenReturn(List.of(new TestResDto(1L, "Test 1", "Description", List.of())));

        AssessmentResDto result = assessmentService.getAssessmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Java Assessment", result.name());
        assertEquals(1, result.tests().size());
    }

    @Test
    void getAssessmentById_NotFound_ShouldThrowException() {
        when(assessmentRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            assessmentService.getAssessmentById(1L);
        });

        assertEquals("Assessment doesn't exist with ID : 1", exception.getMessage());
    }

    @Test
    void getAllAssessments_Success() {
        when(assessmentRepository.findAll()).thenReturn(List.of(assessment));
        when(testClient.getByMultipleTestIds(anyList())).thenReturn(List.of(new TestResDto(1L, "Test 1", "Description", List.of())));

        List<AssessmentResDto> result = assessmentService.getAllAssessments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java Assessment", result.get(0).name());
    }

    @Test
    void deleteAssessment_Success() {
        doNothing().when(assessmentRepository).deleteById(1L);

        assertDoesNotThrow(() -> assessmentService.deleteAssessment(1L));
    }

//    @Test
//    void addTestsToAssessment_Success() {
//        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));
//        when(assessmentTestRepository.saveAll(anyList())).thenReturn(List.of(new AssessmentTest()));
//
//        AssessmentWithTestIdsRes result = assessmentService.addTestsToAssessment(1L, List.of(3L, 4L));
//
//        assertNotNull(result);
//        assertEquals(1L, result.id());
//        assertEquals(2, result.testIds().size());
//    }

}
