package com.assessmentprod.questionBankService.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String optionText;

    private Boolean isCorrect;
}
