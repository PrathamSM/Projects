package com.smeportal.sme_requirements_service.repository;

import com.smeportal.sme_requirements_service.entity.SMERequirements;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SMERequirementsRepository extends JpaRepository<SMERequirements, Long> {
    @Query("SELECT s.numberOfSMEs FROM SMERequirements s WHERE s.projectId = :projectId")
    Integer findNumberOfSMEsByProjectId(@Param("projectId") String projectId);

    Optional<SMERequirements> findByProjectId(String projectId);

}
