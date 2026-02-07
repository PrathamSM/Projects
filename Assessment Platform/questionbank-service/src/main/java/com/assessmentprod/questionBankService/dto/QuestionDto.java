package com.assessmentprod.questionBankService.dto;

import java.util.List;

//public record QuestionDto (Long id, String quesText, List<String> options){
//}

public record QuestionDto (Long quesId, String quesText, List<OpIdAndText> options){
}