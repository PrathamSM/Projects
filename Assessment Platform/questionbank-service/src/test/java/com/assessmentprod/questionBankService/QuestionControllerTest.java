package com.assessmentprod.questionBankService;

import com.assessmentprod.questionBankService.controller.QuestionController;
import com.assessmentprod.questionBankService.dto.CreateQuesReq;
import com.assessmentprod.questionBankService.exception.SubjectNotFoundException;
import com.assessmentprod.questionBankService.service.QuestionService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.assessmentprod.questionBankService.dto.CreateQuesReq;
import com.assessmentprod.questionBankService.dto.OpIdAndText;
import com.assessmentprod.questionBankService.dto.QuestionDto;
import com.assessmentprod.questionBankService.entity.Question;
import com.assessmentprod.questionBankService.entity.QuestionOption;
import com.assessmentprod.questionBankService.repository.QuestionsRepository;
import com.assessmentprod.questionBankService.service.QuestionService;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

//@WebMvcTest(QuestionController.class)
//@ExtendWith({SpringExtension.class, MockitoExtension.class})
//@SpringBootTest
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(questionController).build();
    }

    @Test
    public void TestCreateQuesSuccess() throws Exception {
        CreateQuesReq createQuesReq = new CreateQuesReq("What is PHP?", "Programming", List.of("Op1", "Op2", "Op3", "Op4"), "Op3");

        Mockito.when(questionService.createQuestion(any(CreateQuesReq.class))).thenReturn(new Question());

//        mockMvc.perform(
//
//                MockMvcRequestBuilders.post("/questions")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"quesText\":\"What is PHP?\",\"subject\":\"Programming\",\"options\":[\"Op1\",\"Op2\", \"Op3\", \"Op4\"], \"correctOption\":\"Op3\"}"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Question Created Successfully!"));

        ResponseEntity<String> res = questionController.createQues(createQuesReq);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertEquals("Question Created Successfully!", res.getBody());

    }


    @Test
    public void TestCreateQuesFailure() throws Exception {
        CreateQuesReq createQuesReq = new CreateQuesReq("What is PHP?", "Programming", List.of("Op1", "Op2", "Op3", "Op4"), "Op3");

        Mockito.when(questionService.createQuestion(any(CreateQuesReq.class))).thenReturn(null);

//        mockMvc.perform(
//
//                        MockMvcRequestBuilders.post("/questions")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content("{\"quesText\":\"What is PHP?\",\"subject\":\"Programming\",\"options\":[\"Op1\",\"Op2\", \"Op3\", \"Op4\"], \"correctOption\":\"Op3\"}"))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.content().string("There is an error while creating a question"));

        ResponseEntity<String> response = questionController.createQues(createQuesReq);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("There is an error while creating a question", response.getBody());

    }


    @Test
    public void TestGetQuestionsBySubjectSuccess() throws Exception{
//        QuestionDto questionDto = new QuestionDto(101L, "What is Java?", Collections.emptyList());
//
//        Mockito.when(questionService.getQuestionsBySubject("Java")).thenReturn(List.of(questionDto));
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/questions/subject/Java"))
//                .andExpect(MockMvcResultMatchers.status().isOk());

        String subject = "SQL";
        List<QuestionDto> questionDtos = List.of(new QuestionDto(100L, "SQL stands for", List.of(new OpIdAndText(11L, "Op1"))));
        Mockito.when(questionService.getQuestionsBySubject(subject)).thenReturn(questionDtos);

        ResponseEntity<?> response = questionController.getQuestionsBySubject(subject);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(questionDtos, response.getBody());


    }

    @Test
    public void TestGetQuestionsBySubjectFailure() {
        String subject = "Python";

        // Mock the service to throw SubjectNotFoundException
        Mockito.when(questionService.getQuestionsBySubject(subject)).thenThrow(new SubjectNotFoundException(subject));

        // Call the controller method
//        ResponseEntity<?> response = questionController.getQuestionsBySubject(subject);
//
//        Assertions.assertThrows(SubjectNotFoundException.class,)
//        // Verify the response status and body
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        Assertions.assertEquals("We don't have following subject : Python", response.getBody());

        Throwable exception =  Assertions.assertThrows(SubjectNotFoundException.class, () -> {
            questionController.getQuestionsBySubject(subject);
        });

        Assertions.assertEquals("We don't have following subject : Python",exception.getMessage());
    }

    @Test
    public void TestGetQuestionByIdSuccess() throws Exception{
//        QuestionDto questionDto = new QuestionDto(201L, "SQL stands for", Collections.emptyList());
//
//        Mockito.when(questionService.getQuestionById(201L)).thenReturn(questionDto);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/questions/201"))
//                        .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.quesText").value("SQL stands for"));

        Long questionId = 201L;
        QuestionDto questionDto = new QuestionDto(questionId, "SQL stands for", Collections.emptyList());

        Mockito.when(questionService.getQuestionById(questionId)).thenReturn(questionDto);

        ResponseEntity<QuestionDto> response = questionController.getQuestionByID(questionId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("SQL stands for", response.getBody().quesText());
        Assertions.assertEquals(questionId, response.getBody().quesId());

    }

//    @Test
//    public void TestGetQuestionByIdSuccess() throws Exception{
//        QuestionDto questionDto = new QuestionDto(201L, "SQL stands for", Collections.emptyList());
//
//        Mockito.when(questionService.getQuestionById(201L)).thenReturn(questionDto);
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/questions/201"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.quesText").value("SQL stands for"));
//
//    }


    @Test
    public void testUpdateQuestion() throws Exception{
//        Question updatedQuestion = new Question();
//        updatedQuestion.setQuesText("Updated Question");
//        updatedQuestion.setSubject("Updated Subject");
//
//        Mockito.when(questionService.updateQuestion(eq(301L), any(Question.class))).thenReturn(updatedQuestion);
//
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.put("/questions/301")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"quesText\":\"Updated Question\",\"subject\":\"Updated Subject\"}"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Updated successfully"));

        Long questionId = 301L;
        Question updatedQuestion = new Question();
        updatedQuestion.setQuesText("Which of the following is DML command");
        updatedQuestion.setSubject("SQL");

        Mockito.when(questionService.updateQuestion(eq(questionId), any(Question.class))).thenReturn(updatedQuestion);

        // Create a mock request body
        Question requestBody = new Question();
        requestBody.setQuesText("Updated QuestionWhich of the following is DML command");
        requestBody.setSubject("SQL");

        // Call the controller method
        ResponseEntity<String> response = questionController.updateQuestion(questionId, requestBody);

        // Verify the response status and body
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Updated successfully", response.getBody());
    }


    @Test
    public void testDeleteQuestionByIdSuccess() {
        Long id = 1L;
        Mockito.when(questionService.deleteQuestion(id)).thenReturn(true);

        ResponseEntity<String> response = questionController.deleteQuestionById(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Question deleted successfully!", response.getBody());
    }

    @Test
    public void testDeleteQuestionByIdFailure() {
        Long id = 12L;
        Mockito.when(questionService.deleteQuestion(id)).thenReturn(false);

        ResponseEntity<String> response = questionController.deleteQuestionById(id);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Question not found!", response.getBody());
    }






}
