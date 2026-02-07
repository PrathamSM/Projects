package com.smeportal.profilestatus.controller;

import com.smeportal.profilestatus.dto.ProfileStatusDto;
import com.smeportal.profilestatus.dto.ProfileStatusResponseDto;
import com.smeportal.profilestatus.model.ProfileStatus;
import com.smeportal.profilestatus.response.ApiResponse;
import com.smeportal.profilestatus.service.ProfileStatusService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profile-status")
@CrossOrigin("*")
public class ProfileStatusController {

    @Autowired
    private ProfileStatusService profileStatusService;

    Logger logger = LoggerFactory.getLogger(ProfileStatusController.class);

    @PostMapping
    public ResponseEntity<?> createProfileStatus(@RequestBody ProfileStatus profileStatus) {
        logger.info(profileStatus.getProfileId().toString());
        ProfileStatus createdStatus = profileStatusService.createProfileStatus(profileStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("success", createdStatus, "Profile Status Created Successfully"));

    }

    @GetMapping("/{pId}")
    public ResponseEntity<?> getProfileStatusByProfileId(@PathVariable Long pId) {
        ProfileStatus profileStatus = profileStatusService.getProfileStatusByProfileId(pId);
        return ResponseEntity.status(HttpStatus.OK).body(profileStatus);
//        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("success", profileStatus, "Profile Status Fetched Successfully"));
    }


    @PutMapping("/{pId}")
    public ResponseEntity<?> updateProfileStatus(@PathVariable Long pId, @RequestBody ProfileStatusDto profileStatusDto) {
        ProfileStatus updatedStatus = profileStatusService.updateProfileStatus(pId, profileStatusDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("success", updatedStatus, "Profile Status Updated Successfully"));
    }

    @DeleteMapping("/{pId}")
    public ResponseEntity<?> deleteProfileStatus(@PathVariable Long pId) {
        profileStatusService.deleteProfileStatus(pId);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("success", null, "Profile Status Deleted Succesfully"));
    }

    @GetMapping("/na")
    public ResponseEntity<?> getProfilesWithNotApprovedStatus() {
        List<ProfileStatusResponseDto> profiles = profileStatusService.getProfilesWithNotApprovedStatus();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("success", profiles, "Profiles with not approved status fetched successfully"));
    }

    // API to get the count of NDA signed profiles
    @GetMapping("/nda-signed-count")
    public int getNdaSignedCount() {
        return profileStatusService.getNdaSignedCount();
    }

    @GetMapping("/unapproved-ids")
    public List<Long> getUnapprovedProfileIds() {
        return profileStatusService.getUnapprovedProfileIds();
    }

    @GetMapping("/total")
    public int getTotalCount(){
        return  profileStatusService.getTotal();
    }

}
