package com.assessmentprod.testmgmt.dto;

import java.util.List;

public record TestReq (String name, String description, List<Long> questionIds){
}
