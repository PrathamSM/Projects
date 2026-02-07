//package com.assessmentprod.assessment.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.util.List;
//
//@Entity
//@Data
//@Table(name = "assessment_test")
//public class AssessmentTest {
//
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    @ManyToOne
////    @JoinColumn(name = "test_id")
////    private Test test;
////
////    private Long questionId;
//
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "assessment_id")
//    private Assessment assessment;
//
//    private Long testId;
//
//}


package com.assessmentprod.assessment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "assessment_test")
public class AssessmentTest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    private Long testId;


}
