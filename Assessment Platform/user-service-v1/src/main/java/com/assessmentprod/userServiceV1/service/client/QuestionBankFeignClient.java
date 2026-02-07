package com.assessmentprod.userServiceV1.service.client;

//import com.assessmentprod.userServiceV1.dto.qbs.CreateQuesReq;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "questionbank-service", url = "http://localhost:8081", path = "/questionbank-service")
//@FeignClient(name = "questionbank-service", url = "http://localhost:8081/questions")
public interface QuestionBankFeignClient {

////    @GetMapping(value = "/questions/subject/{sub}", consumes = "application/json")
//    @GetMapping(value = "/subject/{sub}", consumes = "application/json")
//    public ResponseEntity<?> getQuestionsBySubject(@PathVariable("sub") String subject);
//
////    @PostMapping(value ="/questions", consumes = "application/json")
//    @PostMapping(consumes = "application/json")
//    public ResponseEntity<String> createQues(@RequestBody CreateQuesReq req);
//
////    @PostMapping(value = "/questions/{text}", consumes = "application/json")
//    @PostMapping(value = "/{text}", consumes = "application/json")
//    public ResponseEntity<String> demoResponse(@PathVariable String text);
}
