package com.smeportal.proexpservice.repository;

import com.smeportal.proexpservice.model.ProfessionalExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessionalExperienceRepository extends JpaRepository<ProfessionalExperience, Long> {


    public Optional<ProfessionalExperience> findByProfileId(Long id);
}

