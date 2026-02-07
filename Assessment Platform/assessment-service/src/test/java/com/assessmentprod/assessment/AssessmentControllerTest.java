package com.assessmentprod.assessment;

import com.assessmentprod.assessment.controller.AssessmentController;
import com.assessmentprod.assessment.dto.AssessmentReq;
import com.assessmentprod.assessment.dto.AssessmentResDto;
import com.assessmentprod.assessment.dto.AssessmentWithTestIdsRes;
import com.assessmentprod.assessment.service.AssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AssessmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AssessmentService assessmentService;

    @InjectMocks
    private AssessmentController assessmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(assessmentController).build();
    }

    @Test
    void testCreateAssessment() throws Exception {
        AssessmentReq assessmentReq = new AssessmentReq("Test Assessment", List.of(1L, 2L));
        AssessmentWithTestIdsRes response = new AssessmentWithTestIdsRes(1L, "Test Assessment", List.of(1L, 2L));

        when(assessmentService.addAssessment(any(AssessmentReq.class))).thenReturn(response);

        mockMvc.perform(post("/assessments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Assessment\", \"testIds\": [1, 2]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));

        verify(assessmentService, times(1)).addAssessment(any(AssessmentReq.class));
    }

    @Test
    void testGetAssessment() throws Exception {
        Long assessmentId = 1L;
        AssessmentResDto response = new AssessmentResDto(assessmentId, "Test Assessment", List.of());

        when(assessmentService.getAssessmentById(assessmentId)).thenReturn(response);

        mockMvc.perform(get("/assessments/{id}", assessmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assessmentId))
                .andExpect(jsonPath("$.name").value(response.name()));

        verify(assessmentService, times(1)).getAssessmentById(assessmentId);
    }

    @Test
    void testGetAllAssessments() throws Exception {
        List<AssessmentResDto> responseList = Arrays.asList(
                new AssessmentResDto(1L, "Assessment 1", List.of()),
                new AssessmentResDto(2L, "Assessment 2", List.of())
        );

        when(assessmentService.getAllAssessments()).thenReturn(responseList);

        mockMvc.perform(get("/assessments/allAssessment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(assessmentService, times(1)).getAllAssessments();
    }

    @Test
    void testDeleteAssessment() throws Exception {
        Long assessmentId = 1L;

        doNothing().when(assessmentService).deleteAssessment(assessmentId);

        mockMvc.perform(delete("/assessments/{id}", assessmentId))
                .andExpect(status().isOk())
                .andExpect(content().string("Assessment deleted successfully"));

        verify(assessmentService, times(1)).deleteAssessment(assessmentId);
    }

    @Test
    void testUpdateAssessment() throws Exception {
        Long assessmentId = 1L;
        AssessmentReq assessmentReq = new AssessmentReq("Updated Assessment", List.of(1L, 3L));
        AssessmentWithTestIdsRes response = new AssessmentWithTestIdsRes(assessmentId, "Updated Assessment", List.of(1L, 3L));

        when(assessmentService.addTestsToAssessment(anyLong(), any())).thenReturn(response);

        mockMvc.perform(put("/assessments/{id}", assessmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Assessment\", \"testIds\": [1, 3]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));

        verify(assessmentService, times(1)).addTestsToAssessment(anyLong(), any());
    }

}
