//package com.assessmentprod.assessment.dto;
//
//import java.util.List;
//
//public record AssessmentReq(String name, List<Long> testIds) {
//}

package com.assessmentprod.assessment.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AssessmentReq( @NotEmpty(message = "Name is required") String name,
                             @NotEmpty(message = "At least one Test ID is required") List<Long> testIds) {
}