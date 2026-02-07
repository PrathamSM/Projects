package com.assessmentprod.submissionservice.submissionservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.assessmentprod.submissionservice.submissionservice.entity.Submission_entity;


@Repository
public interface SubmissionRepository extends JpaRepository<Submission_entity, Long> {

}
