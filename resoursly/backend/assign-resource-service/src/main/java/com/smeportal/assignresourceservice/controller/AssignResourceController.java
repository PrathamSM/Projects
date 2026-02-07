package com.smeportal.assignresourceservice.controller;

import com.smeportal.assignresourceservice.dto.AssignResourceDTO;
import com.smeportal.assignresourceservice.entity.AssignResource;
import com.smeportal.assignresourceservice.service.AssignResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin("*")
public class AssignResourceController {

    private final AssignResourceService service;

    @Autowired
    private AssignResourceService assignResourceService;

    public AssignResourceController(AssignResourceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AssignResource> createAssignment(@RequestBody AssignResourceDTO assignResourceDTO) {
        return ResponseEntity.ok(service.createAssignment(assignResourceDTO));
    }

    @GetMapping("/alldata")
    public ResponseEntity<List<AssignResource>> getAllAssignments() {
        return ResponseEntity.ok(service.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignResource> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAssignmentById(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<AssignResource>> getAssignmentsByProjectId(@PathVariable String projectId) {
        return ResponseEntity.ok(service.getAssignmentsByProjectId(projectId));
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<AssignResource>> getAssignmentsByProfileId(@PathVariable Long profileId) {
        return ResponseEntity.ok(service.getAssignmentsByProfileId(profileId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignResource> updateAssignment(@PathVariable Long id, @RequestBody AssignResourceDTO assignResourceDTO) {
        return ResponseEntity.ok(service.updateAssignment(id, assignResourceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        service.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/del/{projectId}")
    public ResponseEntity<Void> deleteAssignmentsByProjectId(@PathVariable String projectId) {
        service.deleteAssignmentsByProjectId(projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count/{projectId}")
    public ResponseEntity<Integer> getProfileCountByProjectId(@PathVariable String projectId) {
        int count = service.getProfileCountByProjectId(projectId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/profile-count")
    public ResponseEntity<List<Map<String, Object>>> getProjectProfileCount() {
        List<Map<String, Object>> projectProfileCounts = service.getProjectProfileCount();
        return ResponseEntity.ok(projectProfileCounts);
    }


    @GetMapping("/total-assigned-resources")
    public int getTotalAssignedResources() {
        return service.getTotalAssignedResources();
    }

    @GetMapping("/unique-resources")
    public int getUniqueResourceCount() {
        return service.getUniqueResourceCount();
    }

    @GetMapping("/resource-allocation-percentage")
    public Long getResourceAllocationPercentage() {
        return service.getResourceAllocationPercentage();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateAssignedStatus(@PathVariable Long id, @RequestParam boolean assigned) {
        service.updateAssignedStatus(id, assigned);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-status/{profileId}")
    public ResponseEntity<Void> updateAssignedStatusByProfileId(@PathVariable Long profileId,
                                                                @RequestParam boolean assigned) {
        service.updateAssignedStatusByProfileId(profileId, assigned);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/update-status/pro/{projectId}")
    public ResponseEntity<String> updateAssignedStatusByProjectId(@PathVariable String projectId) {
        service.updateAssignedStatusByProjectId(projectId);
        return ResponseEntity.ok("Assigned to: " + projectId + " Project");
    }

    @PutMapping("/reset/{projectId}")
    public ResponseEntity<String> resetAssignedStatusByProjectId(@PathVariable String projectId) {
        service.resetAssign(projectId);
        return ResponseEntity.ok("Assigned status reset to false for projectId: " + projectId);
    }


    @GetMapping("/profiles/true")
    public ResponseEntity<List<Long>> getProfileIdsByAssignedTrue() {
        return ResponseEntity.ok(service.getProfileIdsByAssignedTrue());
    }

    @GetMapping("/profiles/false")
    public ResponseEntity<List<Long>> getProfileIdsByAssignedFalse() {
        return ResponseEntity.ok(service.getProfileIdsByAssignedFalse());
    }

    @GetMapping("/projects/{profileId}")
    public List<AssignResource> getProjectsByProfileId(@PathVariable Long profileId) {
        List<AssignResource> assignProjects = assignResourceService.getAssignmentsByProfileId(profileId);

        if(assignProjects.isEmpty()) {
            throw new RuntimeException("No projects found for profileId: " + profileId);
        }

        return assignProjects;
    }

    //assign do rtrue by proId and ProfId
    @PutMapping("/assign/{profileId}/{projectId}")
    public ResponseEntity<String> updateAssignedStatusByProfileAndProject(
            @PathVariable Long profileId,
            @PathVariable String projectId) {

        service.updateAssignedStatusByProfileAndProject(profileId, projectId);
        return ResponseEntity.ok("Assigned project no." + projectId +
                " for resource profileId: " + profileId );
    }

    //wikk return the Assign count
    @GetMapping("/assigned/count")
    public ResponseEntity<Integer> getAssignedTrueCount() {
        return ResponseEntity.ok(service.countAssignedTrue());
    }


    @PutMapping("/remove/{profileId}/{projectId}")
    public ResponseEntity<String> unassignResource(
            @PathVariable Long profileId,
            @PathVariable String projectId) {

        service.unassignpro(profileId, projectId);
        return ResponseEntity.ok("removed profile no." +profileId  +
                " from project no." + projectId);
    }

    //assign all false associated with proJ Id
    @PutMapping("/unassignres/{projectId}")
    public ResponseEntity<String> unassignResources(@PathVariable String projectId) {
        service.unassignResourcesByProjectId(projectId);
        return ResponseEntity.ok("All assigned resources set to false for projectId: " + projectId);
    }

    @PutMapping("/updedicate/{profileId}/{projectId}")
    public ResponseEntity<String> updateDedicate(@PathVariable Long profileId, @PathVariable String projectId) {
        assignResourceService.dedistatus(profileId, projectId);
        return ResponseEntity.ok("Dedicated status updated to true successfully");
    }

    @GetMapping("/dedicount")
    public ResponseEntity<Integer> dediCount() {
        int count = assignResourceService.totalDedicated();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/projectcount")
    public ResponseEntity<Integer> procount() {
        int count = assignResourceService.countOngoingpro();
        return ResponseEntity.ok(count);
    }
}
