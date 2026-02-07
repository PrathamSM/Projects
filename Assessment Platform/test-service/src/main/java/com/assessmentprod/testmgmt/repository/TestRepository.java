package com.assessmentprod.testmgmt.repository;

import com.assessmentprod.testmgmt.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {

    //Used inside to get answers by passing question id
//    List<Question> findByIdIn(List<Long> id);

    List<TestEntity> findByIdIn(List<Long> ids);
}
