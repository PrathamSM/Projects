package com.assessmentprod.questionBankService.controller;

import com.assessmentprod.questionBankService.dto.CreateQuesReq;
import com.assessmentprod.questionBankService.dto.QuestionDto;
import com.assessmentprod.questionBankService.dto.QuestionWithCorrectAns;
import com.assessmentprod.questionBankService.entity.Question;
import com.assessmentprod.questionBankService.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService quesService;

    @PostMapping
    public ResponseEntity<String> createQues(@RequestBody CreateQuesReq req) {
        if(quesService.createQuestion(req) != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Question Created Successfully!");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is an error while creating a question");
        }
    }

    @GetMapping("/subject/{sub}")
    public ResponseEntity<?> getQuestionsBySubject(@PathVariable("sub") String subject) {
        List<QuestionDto> questionDetails = quesService.getQuestionsBySubject(subject);

//        if(questionDetails == null || questionDetails.size() == 0) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question with the subject not found!");
//        }

        return ResponseEntity.status(HttpStatus.OK).body(questionDetails);
    }


    //Get Question with ans by sending list of question ids
    @PostMapping("/answers")
    public ResponseEntity<?> getQuestionsWithCorrectAns(@RequestBody List<Long> ids) {
        List<QuestionWithCorrectAns> quetions = quesService.getQuesWithCorrectAns(ids);
        return ResponseEntity.status(HttpStatus.OK).body(quetions);
    }


    //Get Questions for test by providing question ids
    @PostMapping("/ids")
    public ResponseEntity<List<QuestionDto>> getQuestionsByQuesIds(@RequestBody List<Long> ids) {
        List<QuestionDto> quetions = quesService.getQuestionsByQuestionIds(ids);
        return ResponseEntity.status(HttpStatus.OK).body(quetions);
    }

    //Update Question
    @PutMapping("/{id}")public ResponseEntity<String> updateQuestion(@PathVariable Long id, @RequestBody Question updatedQuestion) {
        Question question = quesService.updateQuestion(id, updatedQuestion);
        return ResponseEntity.ok("Updated successfully");
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestionById(@PathVariable Long id) {
        if (quesService.deleteQuestion(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Question deleted successfully!");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found!");
        }
    }


    //GET Question By ID
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto>  getQuestionByID(@PathVariable Long id) {
        QuestionDto questionDto = quesService.getQuestionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(questionDto);
    }

    //Demo
    @PostMapping("/{text}")
    public ResponseEntity<String> demoResponse(@PathVariable String text) {
        return ResponseEntity.status(HttpStatus.OK).body("Hello you text is : " + text);
    }
}
