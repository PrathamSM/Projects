package com.smeportal.assignresourceservice.service;

import com.smeportal.assignresourceservice.dto.AssignResourceDTO;
import com.smeportal.assignresourceservice.entity.AssignResource;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

public interface AssignResourceService {
    AssignResource createAssignment(AssignResourceDTO assignResourceDTO);
    List<AssignResource> getAllAssignments();
    AssignResource getAssignmentById(Long id);
    List<AssignResource> getAssignmentsByProjectId(String projectId);
    List<AssignResource> getAssignmentsByProfileId(Long profileId);
    AssignResource updateAssignment(Long id, AssignResourceDTO assignResourceDTO);
    void deleteAssignment(Long id);

    //06 03
    @Transactional
    void deleteAssignmentsByProjectId(String projectId); // New method

    int getProfileCountByProjectId(String projectId);

    //07 03
    List<Map<String, Object>> getProjectProfileCount();

    int getTotalAssignedResources();
    int getUniqueResourceCount();
    Long getResourceAllocationPercentage();

    void updateAssignedStatus(Long id, boolean assigned);

    void updateAssignedStatusByProfileId(Long profileId, boolean assigned);
    void updateAssignedStatusByProjectId(String projectId);
    void resetAssign(String projectId);

    List<Long> getProfileIdsByAssignedTrue();
    List<Long> getProfileIdsByAssignedFalse();

    void updateAssignedStatusByProfileAndProject(Long profileId, String projectId);
    int countAssignedTrue();

    void unassignpro(Long profileId, String projectId);

    void unassignResourcesByProjectId(String projectId);

    void dedistatus(Long profileId, String projectId);

    int totalDedicated();

    int countOngoingpro();
}
