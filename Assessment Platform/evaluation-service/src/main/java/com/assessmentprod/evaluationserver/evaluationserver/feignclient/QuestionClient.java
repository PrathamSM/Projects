package com.assessmentprod.evaluationserver.evaluationserver.feignclient;

import com.assessmentprod.evaluationserver.evaluationserver.dto.QuestionWithCorrectAns;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "questionbank-service", url = "http://localhost:8081/questions")
public interface QuestionClient {

    @PostMapping("/answers")
    ResponseEntity<List<QuestionWithCorrectAns>> getQuestionsWithCorrectAns(@RequestBody List<Long> ids);

}

