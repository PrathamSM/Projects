package com.smeproject.manage_project_service.feign;

import com.smeproject.manage_project_service.dto.ManageProjectUpdateDTO;
import com.smeproject.manage_project_service.dto.UpdateManagerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "project-team-service", url = "http://localhost:8041/project-team")
public interface ProjectTeamFeignClient {

    @GetMapping("/manager-name/{projectId}")
    String getManagerName(@PathVariable("projectId") String projectId);

    @GetMapping("/manager-email/{projectId}")
    String getManagerEmail(@PathVariable("projectId") String projectId);

    @DeleteMapping("/deletepr/{projectId}")
    ResponseEntity<String> deleteProjectTeam(@PathVariable String projectId);

//    @PatchMapping("/update-manager-name/{projectId}")
//    ResponseEntity<Void> updateManagerName(@PathVariable("projectId") String projectId,
//                                           @RequestBody String managerName);
//
//    @PatchMapping("/update-manager-email/{projectId}")
//    ResponseEntity<Void> updateManagerEmail(@PathVariable("projectId") String projectId,
//                                            @RequestBody String managerEmail);

    @PatchMapping("/update-manager-name/{projectId}")
    ResponseEntity<Void> updateManagerName(@PathVariable("projectId") String projectId,
                                           @RequestBody UpdateManagerDTO managerDTO);

    @PatchMapping("/update-manager-email/{projectId}")
    ResponseEntity<Void> updateManagerEmail(@PathVariable("projectId") String projectId,
                                            @RequestBody UpdateManagerDTO managerDTO);

}
