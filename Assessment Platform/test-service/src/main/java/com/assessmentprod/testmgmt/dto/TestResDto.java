package com.assessmentprod.testmgmt.dto;

import java.util.List;

public record TestResDto(Long testId, String name, String description, List<QuestionDto> questions) {


}
