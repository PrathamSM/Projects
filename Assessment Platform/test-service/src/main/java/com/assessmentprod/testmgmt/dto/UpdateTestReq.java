package com.assessmentprod.testmgmt.dto;

import java.util.List;
public record UpdateTestReq(String name, String description, List<Long> questionIds) {
}
