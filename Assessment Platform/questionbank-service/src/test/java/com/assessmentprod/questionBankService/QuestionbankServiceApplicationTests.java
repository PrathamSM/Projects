package com.assessmentprod.questionBankService;

import com.assessmentprod.questionBankService.service.QuestionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class QuestionbankServiceApplicationTests {

	@Autowired
	QuestionService questionService;

	@Test
	void contextLoads() {
	}

	//@Test
	//void testGetQuestionsByQuestionIds() {
	//	List<Long> ids = List.of(101L, 102L);
//		QuestionService questionService = new QuestionService();
//		questionService.getQuestionsByQuestionIds(ids);

	//	Assertions.assertTrue(questionService.getQuestionsByQuestionIds(ids).size() > 0);
//	}


}
