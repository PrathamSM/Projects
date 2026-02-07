//package com.assessmentprod.assessment.dto;
//
//import com.assessmentprod.assessment.dto.test.TestResDto;
//
//import java.util.List;
//
//public record AssessmentResDto(Long id, String name, List<TestResDto> tests) {
//}


package com.assessmentprod.assessment.dto;

import com.assessmentprod.assessment.dto.test.TestResDto;

import java.util.List;

public record AssessmentResDto(Long id, String name, List<TestResDto> tests) {
}
