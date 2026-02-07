package com.assessmentprod.testmgmt.controller;

import com.assessmentprod.testmgmt.dto.*;
import com.assessmentprod.testmgmt.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestControllerTest {

    @InjectMocks
    private TestController testController;

    @Mock
    private TestService testService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTest() {
        TestReq testReq = new TestReq("Sample Test", "Description", Arrays.asList(1L, 2L));
        TestWithQuesIdsRes testWithQuesIdsRes = new TestWithQuesIdsRes(1L, "Sample Test", "Description", Arrays.asList(1L, 2L));

        when(testService.addTest(any(TestReq.class))).thenReturn(testWithQuesIdsRes);

        ResponseEntity<TestWithQuesIdsRes> response = testController.createTest(testReq);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetTestById() {
        TestResDto testResDto = new TestResDto(1L, "Sample Test", "Description", Arrays.asList());
        when(testService.getTestById(1L)).thenReturn(testResDto);

        ResponseEntity<?> response = testController.getTestById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteTest() {
        doNothing().when(testService).deleteTest(1L);

        ResponseEntity<String> response = testController.deleteTest(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("TestEntity Deleted Successfully!", response.getBody());
    }


    @Test
    public void testGetAllTests_EmptyList() {
        // Arrange
        List<TestWithDescRes> expectedTests = Collections.emptyList();
        when(testService.getAllTests()).thenReturn(expectedTests);

        // Act
        ResponseEntity<List<TestWithDescRes>> response = testController.getAllTests();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTests, response.getBody());
    }

    @Test
    public void testGetAllTests_NullList() {
        // Arrange
        when(testService.getAllTests()).thenReturn(null);

        // Act
        ResponseEntity<List<TestWithDescRes>> response = testController.getAllTests();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testUpdateTest_Success() {
        // Arrange
        Long testId = 1L;
        UpdateTestReq updateTestReq = new UpdateTestReq("Updated Test","This is an updated test",Arrays.asList(1L,2L));
        TestWithQuesIdsRes expectedResponse = new TestWithQuesIdsRes(1L,"Updated Test","This is an updated list",Arrays.asList(1L,2L));
        when(testService.updateTest(testId, updateTestReq)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<TestWithQuesIdsRes> response = testController.updateTest(testId, updateTestReq);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }


    @Test
    public void testGetByMultipleTestIds_EmptyList() {
        // Arrange
        List<Long> testIds = Collections.emptyList();
        when(testService.getTestsByIds(testIds)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<TestResDto>> response = testController.getByMultipleTestIds(testIds);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }



}