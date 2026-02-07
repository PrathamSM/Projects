package com.smeportal.assignresourceservice.repository;

import com.smeportal.assignresourceservice.entity.AssignResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssignResourceRepository extends JpaRepository<AssignResource, Long> {
    List<AssignResource> findByProjectId(String projectId);
    List<AssignResource> findByProfileId(Long profileId);

    @Transactional
    void deleteByProjectId(String projectId);

    @Query("SELECT COUNT(a) FROM AssignResource a WHERE a.projectId = :projectId")
    int countByProjectId(@Param("projectId") String projectId);

    @Query("SELECT COUNT(DISTINCT a.profileId) FROM AssignResource a")
    int countUniqueResources();

    @Query("SELECT COUNT(a.id) FROM AssignResource a")
    int countTotalAssignedResources();

    @Query("SELECT a.projectId, COUNT(a.profileId) FROM AssignResource a GROUP BY a.projectId")
    List<Object[]> getProjectProfileCount();

    @Modifying
    @Query("UPDATE AssignResource a SET a.assigned = :assigned WHERE a.id = :id")
    @Transactional
    void updateAssignedStatus(@Param("id") Long id, @Param("assigned") boolean assigned);

    @Modifying
    @Query("UPDATE AssignResource a SET a.assigned = :assigned WHERE a.profileId = :profileId")
    @Transactional
    void updateAssignedStatusByProfileId(@Param("profileId") Long profileId, @Param("assigned") boolean assigned);

    @Modifying
    @Query("UPDATE AssignResource a SET a.assigned = true WHERE a.projectId = :projectId")
    @Transactional
    void updateAssignedStatusByProjectId(@Param("projectId") String projectId);

    @Query("SELECT a.profileId FROM AssignResource a WHERE a.assigned = true")
    List<Long> findProfileIdsByAssignedTrue();

    @Query("SELECT a.profileId FROM AssignResource a WHERE a.assigned = false")
    List<Long> findProfileIdsByAssignedFalse();

    Optional<AssignResource> findByProfileIdAndProjectId(Long profileId, String projectId);

//    @Query("SELECT COUNT(a) FROM AssignResource a WHERE a.assigned = true")
//    int countByAssignedTrue();
//    @Query("SELECT COUNT(a) FROM AssignResource a")
//    int countByAssignedTrue();

    @Query("SELECT COUNT(a) FROM AssignResource a WHERE a.assigned = true")
    int countByAssignedTrue();

    @Query("SELECT COUNT(a) FROM AssignResource a WHERE a.dedicated = true")
    int countDedicated();

    @Query("SELECT COUNT(DISTINCT a.projectId) FROM AssignResource a WHERE a.assigned = true")
    int countOngoingProjects();
}
