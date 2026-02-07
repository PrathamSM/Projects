package com.smeportal.profileservice.controller;

import com.smeportal.profileservice.dto.CreateProfileReq;
import com.smeportal.profileservice.dto.UpdateProfileReq;
import com.smeportal.profileservice.exception.ProfileNotFoundException;
import com.smeportal.profileservice.model.Profile;
import com.smeportal.profileservice.model.ProfileLifecycle;
import com.smeportal.profileservice.model.ProfileRoleVertical;
import com.smeportal.profileservice.model.Role;
import com.smeportal.profileservice.model.Vertical;
import com.smeportal.profileservice.repository.ProfileRepository;
import com.smeportal.profileservice.response.ApiResponse;
import com.smeportal.profileservice.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profiles")
@CrossOrigin("http://localhost:5173")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    //CREATE PROFILE
    @PostMapping
    public ResponseEntity<?> createProfile(@Valid @RequestBody CreateProfileReq createProfileReq) {
            Profile createdProfile = profileService.createProfile(createProfileReq);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("success", createdProfile.getId(), "Profile Created Successfully"));
    }

    //GET PROFILE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable Long id) throws ProfileNotFoundException {
        Profile fetchedProfile = profileService.getProfileById(id);
//        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("success", fetchedProfile, "Profile Fetched Successfully"));
        return ResponseEntity.status(HttpStatus.OK).body(fetchedProfile);
    }

    //UPDATE PROFILE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfileReq updateProfileReq
    ) throws ProfileNotFoundException {
        Profile updatedProfile = profileService.updateProfile(id, updateProfileReq);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("success", updatedProfile, "Profile updated successfully"));
    }


    @GetMapping("/page")
    public ResponseEntity<?> getAllProfilesByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<Profile> profiles = profileService.getAllProfiles(page, size);
        return ResponseEntity.ok(profiles);
    }


    //GET ALL PROFILES
    @GetMapping
    public ResponseEntity<?> getAllProfiles() {

        return ResponseEntity.status(HttpStatus.OK).body(profileService.getAllProfiles());
    }



    //26/12
    @DeleteMapping("/{id}")
    public ResponseEntity<String>softDeleteProfile(@PathVariable Long id)throws ProfileNotFoundException{
        profileService.softDeleteProfile(id);
        return ResponseEntity.ok("profile marked as deleted.");
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<String>restoreProfile(@PathVariable Long id)throws ProfileNotFoundException{
        profileService.restoreProfile(id);
        return ResponseEntity.ok("profile restore successfully");
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<Profile>> getDeletedProfiles() {
        List<Profile> deletedProfiles = profileService.getDeletedProfiles();
        return ResponseEntity.ok(deletedProfiles);
    }

    @DeleteMapping("/purge")
    public ResponseEntity<String> purgeDeletedProfiles() {
        profileService.purgeDeletedProfiles(Duration.ofDays(7)); // Adjust duration as needed
        return ResponseEntity.ok("Deleted profiles purged successfully.");
    }

    @DeleteMapping("/{id}/par")
    public ResponseEntity<String> parDelete(@PathVariable Long id) throws ProfileNotFoundException{
        profileService.parDeletedProfile(id);
        return ResponseEntity.ok("completely delete profile");
    }


    //06/01
    @GetMapping("/{id}/lifecycle")
    public ResponseEntity<ProfileLifecycle> getProfileLifecycle(@PathVariable Long id) throws ProfileNotFoundException {
        ProfileLifecycle lifecycle = profileService.getProfileLifecycle(id);
        return ResponseEntity.status(HttpStatus.OK).body(lifecycle);
    }


    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getProfileCount() {
        long count = profileRepository.count(); // Use the instance of profileRepository
        Map<String, Long> response = new HashMap<>();
        response.put("profileCount", count);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

     @GetMapping("/grouped-by-country")
    public ResponseEntity<?> getProfilesGroupedByCountry() {
        List<Map<String, Object>> data = profileService.getProfilesGroupedByCountry();

        if (data.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("groupedData", data));
    }

     @GetMapping("/country-count")
    public Map<String, Long> getCountryCount() {
        return profileService.getDistinctCountryCount();
    }

//    //Adding Role and vertical
//    @PostMapping("/{profileId}")
//    public ResponseEntity<Map<String, Long>> addProfileRoleVertical(
//            @PathVariable Long profileId,
//            @RequestBody ProfileRoleVertical request) {
//
//        ProfileRoleVertical createdProfileRoleVertical =
//                profileService.upsertProfileRoleVertical(profileId, request);
//
//        // Return only roleId and verticalId in the response
//        Map<String, Long> response = Map.of(
//                "roleId", createdProfileRoleVertical.getRoleId(),
//                "verticalId", createdProfileRoleVertical.getVerticalId()
//        );
//
//        return ResponseEntity.ok(response);
//    }

     
     
     //Post method for the role and vertical 
     @PostMapping("/{profileId}")
     public ResponseEntity<?> upsertProfileRoleVertical(
             @PathVariable Long profileId,
             @RequestBody ProfileRoleVertical request) {

         ProfileRoleVertical profileRoleVertical = profileService.upsertProfileRoleVertical(profileId, request);


         return ResponseEntity.ok(profileRoleVertical);
     }
     
     
     

//    // PUT API to update role and vertical by profileId
//    @PutMapping("/profile/{profileId}")
//    public ResponseEntity<Map<String, Long>> updateProfileRoleVertical(
//            @PathVariable Long profileId,
//            @RequestBody Map<String, Long> request) {
//
//        Long roleId = request.get("roleId");
//        Long verticalId = request.get("verticalId");
//
//        Map<String, Long> response = profileService.updateProfileRoleVertical(profileId, roleId, verticalId);
//        return ResponseEntity.ok(response);
//    }
    
    @GetMapping("/filtered")
    public Page<Profile> getProfiles(
        @RequestParam(required = false) Long roleId,
        @RequestParam(required = false) Long verticalId,
        @RequestParam(required = false) String country,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return profileService.getFilteredProfiles(roleId, verticalId, country, PageRequest.of(page, size));
    }
    
    @GetMapping("/roles/{profileId}")public List<Role> getRolesByProfileId(@PathVariable Long profileId) {         
   	 return profileService.getRolesByProfileId(profileId); 
   	 }
    
    @GetMapping("/verticals/{profileId}")public List<Vertical> getVerticalsByProfileId(@PathVariable Long profileId) {         
   	 return profileService.getVerticalsByProfileId(profileId); 
   	 }
    
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getUniqueCountries() {
        List<String> countries = profileService.getUniqueCountries();
        return ResponseEntity.ok(countries);
    }

}
