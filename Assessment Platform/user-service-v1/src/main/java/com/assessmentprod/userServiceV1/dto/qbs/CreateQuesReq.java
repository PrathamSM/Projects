package com.assessmentprod.userServiceV1.dto.qbs;

import java.util.List;

public record CreateQuesReq(String quesText, String subject, List<String> options, String correctOption){
}
