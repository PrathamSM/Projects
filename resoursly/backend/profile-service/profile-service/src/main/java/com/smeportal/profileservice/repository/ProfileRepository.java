package com.smeportal.profileservice.repository;

import com.smeportal.profileservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.smeportal.profileservice.dto.LocationResourceProjection;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByPrimaryEmail(String primaryEmail);


    boolean existsBySecondaryEmail(String secondaryEmail);

    Page<Profile> findAll(Pageable pageable);

    // return total count of profiles in intr
    @Query(value = "SELECT COUNT(*) FROM profile", nativeQuery = true)
    int countAllProfiles();

    @Query("SELECT p.country AS location, COUNT(p) AS resources FROM Profile p WHERE p.country is NOT NULL GROUP BY p.country ORDER BY resources DESC")
    List<LocationResourceProjection> getProfilesGroupedByCountry();

     @Query("SELECT COUNT(DISTINCT p.country) FROM Profile p WHERE p.country IS NOT NULL")
    long getDistinctCountryCount();
     
     @Query("SELECT DISTINCT p.country FROM Profile p WHERE p.country IS NOT NULL")
     List<String> getUniqueCountries();
}
