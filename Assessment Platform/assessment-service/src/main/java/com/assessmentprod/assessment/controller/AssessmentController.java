package com.assessmentprod.assessment.controller;

import com.assessmentprod.assessment.dto.AssessmentReq;
import com.assessmentprod.assessment.dto.AssessmentResDto;
import com.assessmentprod.assessment.dto.AssessmentWithTestIdsRes;
import com.assessmentprod.assessment.exception.BadRequestException;
import com.assessmentprod.assessment.service.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;


    @PostMapping
    public ResponseEntity<AssessmentWithTestIdsRes> createAssessment(@Valid @RequestBody AssessmentReq assessmentReq) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(assessmentService.addAssessment(assessmentReq));
    }

    @GetMapping("{id}")
    public ResponseEntity<AssessmentResDto> getAssessment(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(assessmentService.getAssessmentById(id));
    }

    @GetMapping("/allAssessment")
    public ResponseEntity<List<AssessmentResDto>> getAllAssessments() {
        return ResponseEntity.status(HttpStatus.OK).body(assessmentService.getAllAssessments());
    }

    //To be tested
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
        return ResponseEntity.status(HttpStatus.OK).body("Assessment deleted successfully");
    }

    @PutMapping("{id}")
    public ResponseEntity<AssessmentWithTestIdsRes> updateAssessment(@PathVariable Long id, @Valid
    @RequestBody AssessmentReq assessmentReq)
    {
        return ResponseEntity.status(HttpStatus.OK).body(assessmentService.addTestsToAssessment(id, assessmentReq.testIds()));
    }

}
