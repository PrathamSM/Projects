package com.assessmentprod.testmgmt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "test_question")
public class TestQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private TestEntity test;

    private Long questionId;

}
