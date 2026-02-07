package com.assessmentprod.userServiceV1.controller;

//import com.assessmentprod.userServiceV1.dto.qbs.CreateQuesReq;
//import com.assessmentprod.userServiceV1.service.PlatformService;
//import com.assessmentprod.userServiceV1.service.client.QuestionBankFeignClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api")
public class ResourceController {
//
//    @Autowired
//    private PlatformService platformService;
//
//    @Autowired
//    private QuestionBankFeignClient questionBankFeignClient;
//
////    @GetMapping("/assessments")
////    @ResponseBody
////    public String assessmentAPI() {
////        return "You just called the assessment API!";
////    }
//
//    @GetMapping("/assessments")
//    public ResponseEntity<?> getAssessments() {
//        return ResponseEntity.status(HttpStatus.OK).body(platformService.getAssessments());
//    }
//
//    //Demo of Openfeign - getting questios from question bank service
//    @GetMapping("/questions/subject/{sub}")
//    public ResponseEntity<?> getQuestionsBySubject(@PathVariable("sub") String subject) {
//        return questionBankFeignClient.getQuestionsBySubject(subject);
//    }
//
//    //Creating Question
//    @PostMapping("/questions")
//    public ResponseEntity<String> createQuestion(@RequestBody CreateQuesReq req) {
//        return questionBankFeignClient.createQues(req);
//    }
//
//    @PostMapping("/questions/{text}")
//    public ResponseEntity<String> createQuestion(@PathVariable String text) {
//        return questionBankFeignClient.demoResponse(text);
//    }
}
