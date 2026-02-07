package com.smeportal.assignresourceservice.service;

import com.smeportal.assignresourceservice.client.ProjectServiceClient;
import com.smeportal.assignresourceservice.dto.AssignResourceDTO;
import com.smeportal.assignresourceservice.entity.AssignResource;
import com.smeportal.assignresourceservice.exception.ResourceNotFoundException;
import com.smeportal.assignresourceservice.repository.AssignResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssignResourceServiceImpl implements AssignResourceService {

    private final AssignResourceRepository repository;
    private final ProjectServiceClient projectServiceClient;  // Add this line

    @Autowired
    private AssignResourceRepository assignResourceRepository;

    // Constructor injection
    public AssignResourceServiceImpl(AssignResourceRepository repository, ProjectServiceClient projectServiceClient) {
        this.repository = repository;
        this.projectServiceClient = projectServiceClient;  // Initialize projectServiceClient
    }

    @Override
    public AssignResource createAssignment(AssignResourceDTO assignResourceDTO) {
        AssignResource assignResource = new AssignResource();
        assignResource.setProfileId(assignResourceDTO.getProfileId());
        assignResource.setProjectId(assignResourceDTO.getProjectId());
        assignResource.setClientName(assignResourceDTO.getClientName());
        assignResource.setAssigned(assignResourceDTO.isAssigned());
        assignResource.setProjectName(assignResourceDTO.getProjectName());
        assignResource.setDedicated(assignResourceDTO.isDedicated());
        return repository.save(assignResource);
    }

    @Override
    public List<AssignResource> getAllAssignments() {
        return repository.findAll();
    }

    @Override
    public AssignResource getAssignmentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
    }

    @Override
    public List<AssignResource> getAssignmentsByProjectId(String projectId) {
        return repository.findByProjectId(projectId);
    }

    @Override
    public List<AssignResource> getAssignmentsByProfileId(Long profileId) {
        return repository.findByProfileId(profileId);
    }

    @Override
    public AssignResource updateAssignment(Long id, AssignResourceDTO assignResourceDTO) {
        AssignResource assignResource = getAssignmentById(id);
        assignResource.setProfileId(assignResourceDTO.getProfileId());
        assignResource.setProjectId(assignResourceDTO.getProjectId());
        assignResource.setClientName(assignResourceDTO.getClientName()); // Update clientName
        assignResource.setAssigned(assignResourceDTO.isAssigned());
        assignResource.setAssigned(assignResourceDTO.isAssigned());
        assignResource.setDedicated(assignResourceDTO.isDedicated());
        return repository.save(assignResource);
    }

    @Override
    public void deleteAssignment(Long id) {
        AssignResource assignResource = getAssignmentById(id);
        repository.delete(assignResource);
    }

    //06 03
    @Override
    public void deleteAssignmentsByProjectId(String projectId) {
        List<AssignResource> assignments = repository.findByProjectId(projectId);

        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("No Assign resource found for projectId: " + projectId);
        }

        repository.deleteByProjectId(projectId);
    }

    @Override
    public int getProfileCountByProjectId(String projectId) {
        return repository.countByProjectId(projectId);
    }

    @Override
    public List<Map<String, Object>> getProjectProfileCount() {
        List<Object[]> result = repository.getProjectProfileCount();

        List<Map<String, Object>> projectProfileCounts = new ArrayList<>();

        for (Object[] record : result) {
            Map<String, Object> projectProfileCount = new HashMap<>();
            projectProfileCount.put("projectId", record[0]);
            projectProfileCount.put("profileCount", record[1]);
            projectProfileCounts.add(projectProfileCount);
        }

        return projectProfileCounts;
    }

    @Override
    public int getTotalAssignedResources() {
        return repository.countTotalAssignedResources();
    }

    @Override
    public int getUniqueResourceCount() {
        return repository.countUniqueResources();
    }


    @Override
    public Long getResourceAllocationPercentage() {
        int uniqueResourceCount = repository.countUniqueResources();
        Long totalProjectCount = projectServiceClient.getProjectCount();

        if (totalProjectCount == 0) {
            return 0L; // Avoid division by zero
        }

        return (uniqueResourceCount * 100L) / totalProjectCount;
    }

    @Override
    public void updateAssignedStatus(Long id, boolean assigned) {
        repository.updateAssignedStatus(id, assigned);
    }

    @Override
    public void updateAssignedStatusByProfileId(Long profileId, boolean assigned) {
        repository.updateAssignedStatusByProfileId(profileId, assigned);
    }

    @Override
    public void updateAssignedStatusByProjectId(String projectId) {
        repository.updateAssignedStatusByProjectId(projectId);
    }

    @Override
    public void resetAssign(String projectId) {
        List<AssignResource> assignments = repository.findByProjectId(projectId);
        for (AssignResource assignment : assignments) {
            assignment.setAssigned(false);
        }
        repository.saveAll(assignments);
    }

    @Override
    public List<Long> getProfileIdsByAssignedTrue() {
        return repository.findProfileIdsByAssignedTrue();
    }

    @Override
    public List<Long> getProfileIdsByAssignedFalse() {
        return repository.findProfileIdsByAssignedFalse();
    }


    @Override
    public void updateAssignedStatusByProfileAndProject(Long profileId, String projectId) {
        AssignResource assignResource = repository.findByProfileIdAndProjectId(profileId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found for profileId: "
                        + profileId + " and projectId: " + projectId));

        assignResource.setAssigned(true);
        repository.save(assignResource);
    }

    @Override
    public int countAssignedTrue() {
        return repository.countByAssignedTrue();
    }

    @Override
    public void unassignpro(Long profileId, String projectId) {
        AssignResource assignResource = repository.findByProfileIdAndProjectId(profileId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found for profileId: "
                        + profileId + " and projectId: " + projectId));

        assignResource.setAssigned(false);  // Set assigned = false
        repository.save(assignResource);
    }

    @Override
    public void unassignResourcesByProjectId(String projectId) {
        List<AssignResource> assignments = repository.findByProjectId(projectId);

        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("No assignments found for projectId: " + projectId);
        }

        for (AssignResource assignment : assignments) {
            assignment.setAssigned(false); // Set assigned = false
        }

        repository.saveAll(assignments);
    }

    @Override
    public void dedistatus(Long profileId, String projectId) {
        AssignResource assignResource = repository.findByProfileIdAndProjectId(profileId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found for profileId: "
                        + profileId + " and projectId: " + projectId));

        assignResource.setDedicated(true); // Set dedicated to true
        repository.save(assignResource);
    }

    @Override
    public int totalDedicated() {
        return repository.countDedicated();
    }


    //count ongoing projects
    @Override
    public int countOngoingpro(){
        return repository.countOngoingProjects();
    }


}
