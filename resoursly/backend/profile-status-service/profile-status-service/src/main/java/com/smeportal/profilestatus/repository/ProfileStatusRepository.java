package com.smeportal.profilestatus.repository;

import com.smeportal.profilestatus.model.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileStatusRepository extends JpaRepository<ProfileStatus, Long> {

    public Optional<ProfileStatus> findByProfileId(Long pId);

    // New method to find profiles that are not approved
    List<ProfileStatus> findByIsApprovedFalse();

    @Query("SELECT COUNT(p) FROM ProfileStatus p WHERE p.ndaSigned = true")
    int countByNdaSignedTrue();

    @Query("SELECT COUNT(p) FROM ProfileStatus p")
    int countTotal();

    @Query("SELECT p.profileId FROM ProfileStatus p WHERE p.isApproved = false")
    List<Long> findUnapprovedProfileIds();
}
