package com.assessmentprod.questionBankService.repository;

import com.assessmentprod.questionBankService.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionsRepository extends JpaRepository<Question, Long> {


    Optional<List<Question>> findBySubject(String subject);

    //Used inside to get answers by passing question id
    List<Question> findByIdIn(List<Long> id);
}
