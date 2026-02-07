package com.assessmentprod.testmgmt.service.client;

import com.assessmentprod.testmgmt.dto.QuestionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "questionbank-service", url = "http://localhost:8081/questions")
public interface QuestionBankClient {

    @PostMapping("/ids")
    public List<QuestionDto> getQuestionsByQuesIds(@RequestBody List<Long> ids);
}
