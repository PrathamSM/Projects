package com.assessmentprod.testmgmt.repository;

import com.assessmentprod.testmgmt.entity.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    List<TestQuestion> findByTestId(Long testId);

    void deleteByTestId(Long testId);
}
