package com.smeportal.searchms.service;

import com.smeportal.searchms.client.FeedbackRatingServiceClient;
import com.smeportal.searchms.client.ProfessionalExperienceServiceClient;
import com.smeportal.searchms.client.ProfileServiceClient;
import com.smeportal.searchms.client.ProfileStatusServiceClient;
import com.smeportal.searchms.model.FeedbackRating;
import com.smeportal.searchms.model.ProfessionalExperienceResponseDTO;
import com.smeportal.searchms.model.ProfileResponse;
import com.smeportal.searchms.model.ProfileStatus;
import com.smeportal.searchms.model.Role;
import com.smeportal.searchms.model.Vertical;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
//Updated upstream
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.HashMap;
//>>>>>>> Stashed changes
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);
    @Autowired
    private ProfileServiceClient profileServiceClient;

    @Autowired
    private ProfileStatusServiceClient profileStatusServiceClient;

    @Autowired
    private FeedbackRatingServiceClient feedbackRatingServiceClient;

    @Autowired
    private ProfessionalExperienceServiceClient professionalExperienceServiceClient;

    // DEMO GET ALL PROFILES
    public List<ProfileResponse> getAllProfiles() {
        List<ProfileResponse> profiles = profileServiceClient.getAllProfiles();
        return profiles.stream()
                .map(this::safeEnrichProfile)
                .toList();
    }

    // DEMO TO GET DISTINCT COUNTRY
    public Map<String, Object> getDistinctCountryCount() {
        List<ProfileResponse> profiles = profileServiceClient.getAllProfiles();

        Set<String> distinctCountries = profiles.stream()
                .map(ProfileResponse::getCountry)
                .filter(country -> country != null && !country.trim().isEmpty())
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Map<String, Object> response = new HashMap<>();
        response.put("countryCount", distinctCountries.size()); // Rename key to "country"

        return response;
    }

    public Page<ProfileResponse> getApprovedProfiles(Long roleId, Long verticalId, String country, int page, int size) {
        // Fetch profiles based on roleId and verticalId
        Page<ProfileResponse> profiles = profileServiceClient.getProfiles(roleId, verticalId, country, page, size);

        // Process and filter profiles
        List<ProfileResponse> approvedProfiles = profiles.getContent().stream()
                .map(this::safeEnrichProfile)
                .filter(profile -> profile.getProfileStatus() != null
                        && Boolean.TRUE.equals(profile.getProfileStatus().getApproved()))
                .toList();

        // Return new Page object with filtered approved profiles
        return new PageImpl<>(approvedProfiles, profiles.getPageable(), profiles.getTotalElements());
    }

    // DEMO GET SINGLE PROFILE
    public ProfileResponse getProfileById(Long id) {
        ProfileResponse profile = profileServiceClient.getProfileById(id);
        return safeEnrichProfile(profile);
    }

    // APPROVED PROFILES
    public List<ProfileResponse> getApprovedProfiles() {
        List<ProfileResponse> profiles = profileServiceClient.getAllProfiles();
        return profiles.stream()
                .map(this::safeEnrichProfile)
                .filter(profie -> profie.getProfileStatus() != null
                        && Boolean.TRUE.equals(profie.getProfileStatus().getApproved()))
                .toList();
    }

    public List<ProfileResponse> getUnApprovedProfiles() {
        List<ProfileResponse> profiles = new ArrayList<>();
     List<Long> unApprovedProfileNoList = profileStatusServiceClient.getUnapprovedProfileIds();
     for(Long id : unApprovedProfileNoList) {
        ProfileResponse profile = profileServiceClient.getProfileById(id);
        profiles.add(profile);
     }
     return profiles.stream()
     .map(this::safeEnrichProfile)
     .toList();
    }

    private ProfileResponse safeEnrichProfile(ProfileResponse profile) {
        // Long profileId = profile.getId();

        // ProfileStatus profileStatus =
        // profileStatusServiceClient.getProfileStatusByProfileId(profileId);
        // profile.setProfileStatus(profileStatus);
        //
        // FeedbackRating feedbackRating =
        // feedbackRatingServiceClient.getFeedbackByProfileId(profileId);
        // profile.setFeedbackRating(feedbackRating);
        //
        // return profile;

        Long profileId = profile.getId();
        try {
            ProfileStatus profileStatus = profileStatusServiceClient.getProfileStatusByProfileId(profileId);
            profile.setProfileStatus(profileStatus);
        } catch (FeignException.NotFound ex) {
            log.warn("Profile Status No found for profile id {} : {}", profileId, ex.getMessage());
        } catch (FeignException ex) {
            log.error("Error fetching ProfileStatus for Profile ID {}: {}", profileId, ex.getMessage());
            profile.setProfileStatus(null); // Set default or null
        }

        try {
            FeedbackRating feedbackRating = feedbackRatingServiceClient.getFeedbackByProfileId(profileId);
            profile.setFeedbackRating(feedbackRating);
        } catch (FeignException.NotFound ex) {
            log.warn("Feedback Rating Not found for profile id {} : {}", profileId, ex.getMessage());
        } catch (FeignException ex) {
            log.error("Feedback Rating ProfileStatus for Profile ID {}: {}", profileId, ex.getMessage());
            profile.setProfileStatus(null); // Set default or null
        }

        // Fetch ProfessionalExperience
        try {
            ProfessionalExperienceResponseDTO experience = professionalExperienceServiceClient
                    .getProfessionalExperience(profileId);
            profile.setProfessionalExperience(experience);
        } catch (FeignException.NotFound ex) {
            log.warn("Professional Experience not found for profile ID {}: {}", profileId, ex.getMessage());
        } catch (FeignException ex) {
            log.error("Error fetching ProfessionalExperience for profile ID {}: {}", profileId, ex.getMessage());
            profile.setProfessionalExperience(null);
        }

        // Map ProfileLifecycle details
        if (profile.getProfileLifecycle() != null) {
            ProfileResponse.ProfileLifecycle lifecycle = profile.getProfileLifecycle();
            profile.getProfileLifecycle().setDeleted(lifecycle.isDeleted());
            profile.getProfileLifecycle().setDeletedAt(lifecycle.getDeletedAt());
            profile.getProfileLifecycle().setRestoreAt(lifecycle.getRestoreAt());
        }
        
        try {
            List<Role> role = profileServiceClient.getRolesByProfileId(profileId);
            profile.setRole(role);
        } catch (FeignException.NotFound ex) {
            log.warn("Role not found for profile ID {}: {}", profileId, ex.getMessage());
        } catch (FeignException ex) {
            log.error("Error fetching Role for profile ID {}: {}", profileId, ex.getMessage());
            profile.setRole(null);
        }
 
        try {
            List<Vertical> verticals = profileServiceClient.getVerticalsByProfileId(profileId);
            profile.setVertical(verticals);
        } catch (FeignException.NotFound ex) {
            log.warn("Vertical not found for profile ID {}: {}", profileId, ex.getMessage());
        } catch (FeignException ex) {
            log.error("Vertical fetching Role for profile ID {}: {}", profileId, ex.getMessage());
            profile.setVertical(null);
        }

        return profile;
    }


    public String getProfileCount() {
        return profileServiceClient.getProfileCount();
    }
}
