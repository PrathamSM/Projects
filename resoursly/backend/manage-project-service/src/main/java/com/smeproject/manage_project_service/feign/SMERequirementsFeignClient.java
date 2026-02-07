package com.smeproject.manage_project_service.feign;

import com.smeproject.manage_project_service.dto.ManageProjectUpdateDTO;
import com.smeproject.manage_project_service.dto.ProjectSMEDetailsDTO;
import com.smeproject.manage_project_service.dto.UpdateSMERequirementDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "sme-requirements-service", url = "http://localhost:8042/sme-requirements")
public interface SMERequirementsFeignClient {

    @GetMapping("/no-sme/{projectId}")
    Integer getNumberOfSMEs(@PathVariable("projectId") String projectId);

    @DeleteMapping("/deletepro/{projectId}")
    ResponseEntity<String> deleteSMERequirements(@PathVariable String projectId);

//    @PatchMapping("/updatepro/{projectId}")
//    ResponseEntity<Void> updateRequirementByProjectId(@PathVariable("projectId") String projectId,
//                                                      @RequestBody ManageProjectUpdateDTO updateDTO);

    @PatchMapping("/updatepro/{projectId}")
    ResponseEntity<Void> updateRequirementByProjectId(@PathVariable("projectId") String projectId,
                                                      @RequestBody UpdateSMERequirementDTO updateDTO);

    @GetMapping("/primarySkill/{projectId}")
    String getFirstRequiredSkill(@PathVariable String projectId);

    @GetMapping("/proj-details")
    List<ProjectSMEDetailsDTO> getProjectSMEDetails();

}
