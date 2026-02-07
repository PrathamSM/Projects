package com.assessmentprod.questionBankService;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class QuestionServiceTest {

    @Mock
    private QuestionsRepository questionsRepository;

    @InjectMocks
    private QuestionService questionService;


    @Test
    public void testCreateQuestion() {
        CreateQuesReq createQuesReq = new CreateQuesReq("HTML stands for", "HTML",
                                List.of("HighText Machine Language", "HyperText and links Markup Language", "HyperText Markup Language", "None of these"), "HyperText Markup Language");

        Question question = new Question();
        question.setQuesText("HTML stands for");
        question.setSubject("HTML");
        when(questionsRepository.save(any(Question.class))).thenReturn(question);

        Question createdQuestion = questionService.createQuestion(createQuesReq);

//        Assertions.assertNotNull(createdQuestion);      //To ensure the created question should not null
//        Assertions.assertEquals("HTML stands for", createdQuestion.getQuesText());
//        Assertions.assertEquals("HTML", createdQuestion.getSubject());

        Assertions.assertNotNull(createdQuestion);
        Assertions.assertEquals("HTML stands for", createdQuestion.getQuesText());
        Assertions.assertEquals("HTML", createdQuestion.getSubject());

    }

    @Test
    public void testGetQuestionsBySubject() {

        Question question = new Question();

        question.setId(1L);
        question.setQuesText("What is Java?");
        question.setSubject("Java");
        question.setOptions(Collections.emptyList());

        when(questionsRepository.findBySubject("Java")).thenReturn(Optional.of(List.of(question)));

        List<QuestionDto> questionDtos = questionService.getQuestionsBySubject("Java");

        Assertions.assertNotNull(questionDtos);
        Assertions.assertEquals(1, questionDtos.size());


    }

//    @Test
//    public void testGetQuestionsBySubject2() {
//        String subject = "Math";
//        List<Question> questions = List.of(new Question(1L, "Sample Question", "Math", List.of(new QuestionOption(1L, , "Option1", true))));
//        when(questionsRepository.findBySubject(subject)).thenReturn(Optional.of(questions));
//
//        List<QuestionDto> result = questionService.getQuestionsBySubject(subject);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertFalse(result.isEmpty());
//        Assertions.assertEquals("Sample Question", result.);
//    }

    @Test
    public void testGetQuestionById() {
        Question question = new Question();
        question.setId(100L);
        question.setQuesText("HTML stands for");
        question.setSubject("HTML");


        question.setOptions(Collections.emptyList());

        when(questionsRepository.findById(100L)).thenReturn(Optional.of(question));

        QuestionDto questionDto = questionService.getQuestionById(100L);

        Assertions.assertNotNull(questionDto);
        Assertions.assertEquals("HTML", question.getSubject());
    }

    @Test
    public void testUpdateQuestion_Success() {
        Long id = 1L;
        Question existingQuestion = new Question();
        existingQuestion.setId(id);
        existingQuestion.setQuesText("2 + 2 = ?");
        existingQuestion.setSubject("Math");
        existingQuestion.setOptions(new ArrayList<>());

        Question updatedQuestion = new Question();
        updatedQuestion.setQuesText("Molecular formula of water");
        updatedQuestion.setSubject("Science");


        QuestionOption updatedOption = new QuestionOption();
        updatedOption.setId(1L);
        updatedOption.setOptionText("H20");
        updatedOption.setIsCorrect(true);
        updatedOption.setQuestion(updatedQuestion);
        updatedQuestion.setOptions(List.of(updatedOption));

        Mockito.when(questionsRepository.findById(id)).thenReturn(Optional.of(existingQuestion));
        Mockito.when(questionsRepository.save(any(Question.class))).thenReturn(updatedQuestion);

        Question result = questionService.updateQuestion(id, updatedQuestion);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Molecular formula of water", result.getQuesText());
        Assertions.assertEquals("Science", result.getSubject());
        Assertions.assertEquals(1, result.getOptions().size());
        Assertions.assertEquals("H20", result.getOptions().get(0).getOptionText());
    }

    @Test
    public void testDeleteQuestion() {
        Question question = new Question();
        question.setId(200L);
        question.setQuesText("Which of the following is DDL command");
        question.setSubject("SQL");
        question.setOptions(Collections.emptyList());


        when(questionsRepository.findById(200L)).thenReturn(Optional.of(question));

        boolean isDeleted = questionService.deleteQuestion(200L);

        Assertions.assertTrue(isDeleted);

        verify(questionsRepository, times(1)).delete(any(Question.class));

    }



}
