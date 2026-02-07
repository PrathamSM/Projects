package com.assessmentprod.assessment.service.client;

import com.assessmentprod.assessment.dto.test.TestResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "test-service", url = "http://localhost:8082/tests")
//@FeignClient(name = "questionbank-service", url = "http://localhost:8081/questions")
public interface TestClient {

    @PostMapping("/ids")
    public List<TestResDto> getByMultipleTestIds(@RequestBody List<Long> testIds);
}
