package com.assessmentprod.testmgmt.dto;

import java.util.List;

public record TestWithQuesIdsRes(Long id, String name, String description, List<Long> questionIds) {
}
