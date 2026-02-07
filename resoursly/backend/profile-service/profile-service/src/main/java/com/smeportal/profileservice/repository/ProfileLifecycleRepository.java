package com.smeportal.profileservice.repository;

import com.smeportal.profileservice.model.ProfileLifecycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileLifecycleRepository extends JpaRepository<ProfileLifecycle, Long> {
    Optional<ProfileLifecycle> findByProfileId(Long profileId);
}
