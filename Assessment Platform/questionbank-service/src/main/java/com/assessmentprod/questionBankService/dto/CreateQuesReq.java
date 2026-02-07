package com.assessmentprod.questionBankService.dto;

import java.util.List;

public record CreateQuesReq (String quesText, String subject, List<String> options, String correctOption){
}
