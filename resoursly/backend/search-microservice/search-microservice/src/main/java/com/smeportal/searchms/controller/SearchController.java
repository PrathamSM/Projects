package com.smeportal.searchms.controller;

import com.smeportal.searchms.model.ProfileResponse;
import com.smeportal.searchms.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin("http://localhost:5173")
public class SearchController {

    @Autowired
    private SearchService searchService;


    @GetMapping("/profiles")
    public ResponseEntity<?> getAllProfiles() {
        List<ProfileResponse> profiles = searchService.getAllProfiles();
        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }

    @GetMapping("/allCountry/count")
    public ResponseEntity<?> getDistinctCountryCount() {
        Map<String, Object> response = searchService.getDistinctCountryCount();
        //return ResponseEntity.ok(response);
        
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("countryCount", response.get("countryCount")));

    }


    @GetMapping("/profiles/{pId}")
    public ResponseEntity<?> getProfileById(@PathVariable Long pId) {
        ProfileResponse profile = searchService.getProfileById(pId);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }


    //GET APPROVED PROFILES
    @GetMapping("/profiles/approved")
    public ResponseEntity<?> getApprovedProfiles() {
        List<ProfileResponse> approvedProfiles = searchService.getApprovedProfiles();
        return ResponseEntity.status(HttpStatus.OK).body(approvedProfiles);
    }

    @GetMapping("/profiles/unapproved")
    public ResponseEntity<?> getUnApprovedProfiles() {
        List<ProfileResponse> unApprovedProfiles = searchService.getUnApprovedProfiles();
        return ResponseEntity.status(HttpStatus.OK).body(unApprovedProfiles);
    }

 //GET APPROVED PROFILES
    @GetMapping("/profiles/total")
    public ResponseEntity<?> getProfilesTotal() {
        //List<ProfileResponse> approvedProfiles = searchService.getApprovedProfiles();
        //return ResponseEntity.status(HttpStatus.OK).body(Map.of("approvedCount", approvedProfiles.size()));
        List<ProfileResponse> approvedProfiles = searchService.getAllProfiles();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("approvedCount", approvedProfiles.size()));

    }

    @GetMapping("/search/profiles/approved")
    public ResponseEntity<?> getApprovedProfiles(
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Long verticalId,
            @RequestParam(required = false) String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<ProfileResponse> approvedProfiles = searchService.getApprovedProfiles(roleId, verticalId, country,  page, size);
        return ResponseEntity.ok(approvedProfiles);
    }

    @GetMapping("/profiles/count")
    public ResponseEntity<?> getProfileCount() {
        String count = searchService.getProfileCount();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

}
