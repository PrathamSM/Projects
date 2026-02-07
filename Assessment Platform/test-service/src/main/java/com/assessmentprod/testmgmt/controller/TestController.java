package com.assessmentprod.testmgmt.controller;

import com.assessmentprod.testmgmt.dto.*;
import com.assessmentprod.testmgmt.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping
    public ResponseEntity<TestWithQuesIdsRes> createTest(@RequestBody TestReq testReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.addTest(testReq));
    }

    @GetMapping("/{testId}")
    public ResponseEntity<?> getTestById(@PathVariable Long testId) {
        TestResDto testResDto = testService.getTestById(testId);

        if(testResDto != null) {
            return ResponseEntity.status(HttpStatus.OK).body(testResDto);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TestEntity doesn't exist with ID : " + testId);
    }


    @GetMapping
    public ResponseEntity<List<TestWithDescRes>> getAllTests() {
        return ResponseEntity.status(HttpStatus.OK).body(testService.getAllTests());
    }


    //Update TestEntity
    @PutMapping("/{testId}")
    public ResponseEntity<TestWithQuesIdsRes> updateTest(@PathVariable Long testId, @RequestBody UpdateTestReq updateTestReq) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.updateTest(testId, updateTestReq));
    }


    //Delete TestEntity
    @DeleteMapping("/{testId}")
    public ResponseEntity<String> deleteTest(@PathVariable Long testId) {
        testService.deleteTest(testId);
        return ResponseEntity.status(HttpStatus.OK).body("TestEntity Deleted Successfully!");
    }


    //Get Info of List of tests
    //Get Question with ans by sending list of question ids
    @PostMapping("/ids")public ResponseEntity<List<TestResDto>> getByMultipleTestIds(@RequestBody List<Long> testIds) {
        List<TestResDto> tests = testService.getTestsByIds(testIds);
        return ResponseEntity.status(HttpStatus.OK).body(tests);}

}
