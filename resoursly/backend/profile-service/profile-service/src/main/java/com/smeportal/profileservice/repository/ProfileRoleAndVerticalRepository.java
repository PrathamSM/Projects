package com.smeportal.profileservice.repository;

import com.smeportal.profileservice.model.Profile;
import com.smeportal.profileservice.model.ProfileRoleVertical;
import com.smeportal.profileservice.model.Role;
import com.smeportal.profileservice.model.Vertical;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRoleAndVerticalRepository extends JpaRepository<ProfileRoleVertical, Long> {

    Optional<ProfileRoleVertical> findByProfileId(Long profileId);
    
    @Query("SELECT r FROM Role r " +
            "JOIN ProfileRoleVertical prv ON r.id = prv.roleId " +
            "WHERE prv.profileId = :profileId")
     List<Role> findRolesByProfileId(@Param("profileId") Long profileId);
  
     @Query("SELECT v FROM Vertical v " +
            "JOIN ProfileRoleVertical prv ON v.id = prv.verticalId " +
            "WHERE prv.profileId = :profileId")
     List<Vertical> findVerticalsByProfileId(@Param("profileId") Long profileId);
     @Query("SELECT p FROM Profile p " +
    	       "JOIN ProfileRoleVertical prv ON p.id = prv.profileId " +
    	       "JOIN Role r ON prv.roleId = r.id " +
    	       "JOIN Vertical v ON prv.verticalId = v.id " +
    	       "WHERE (:roleId IS NULL OR r.id = :roleId) " +
    	       "AND (:verticalId IS NULL OR v.id = :verticalId) " +
    	       "AND (:country IS NULL OR p.country = :country)")
    	Page<Profile> findProfilesByRoleVerticalAndCountry(
    	    @Param("roleId") Long roleId,
    	    @Param("verticalId") Long verticalId,
    	    @Param("country") String country,
    	    Pageable pageable
    	);

}