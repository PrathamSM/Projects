package com.smeportal.profileservice.service;

import com.smeportal.profileservice.client.ProfileStatusFeignClient;
import com.smeportal.profileservice.dto.CreateProfileReq;
import com.smeportal.profileservice.dto.ProfileStatusDto;
import com.smeportal.profileservice.dto.UpdateProfileReq;
import com.smeportal.profileservice.exception.DuplicateEmailException;
import com.smeportal.profileservice.exception.ProfileNotFoundException;
import com.smeportal.profileservice.exception.RestoreWithinOneMinuteException;
import com.smeportal.profileservice.model.Profile;
import com.smeportal.profileservice.model.ProfileLifecycle;
import com.smeportal.profileservice.model.ProfileRoleVertical;
import com.smeportal.profileservice.model.Role;
import com.smeportal.profileservice.model.Vertical;
import com.smeportal.profileservice.repository.ProfileLifecycleRepository;
import com.smeportal.profileservice.repository.ProfileRepository;
import com.smeportal.profileservice.repository.ProfileRoleAndVerticalRepository;
import com.smeportal.profileservice.repository.ProfileRoleVerticalRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.smeportal.profileservice.dto.LocationResourceProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileService {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private ProfileMapper profileMapper;

	@Autowired
	ProfileRoleAndVerticalRepository profileRoleAndVerticalRepository;

	@Autowired
	private ProfileLifecycleRepository profileLifecycleRepository;

	@Autowired
	private ProfileStatusFeignClient profileStatusFeignClient;

	public List<Profile> getAllProfiles() {
		return profileRepository.findAll();
	}

	public Page<Profile> getAllProfiles(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return profileRepository.findAll(pageable);
	}

	// CREATE A PROFILE
	public Profile createProfile(@Valid CreateProfileReq createProfileReq) {

		if (profileRepository.existsByPrimaryEmail(createProfileReq.getPrimaryEmail())) {
			throw new DuplicateEmailException("Primary email " + createProfileReq.getPrimaryEmail()
					+ " is already associated with an existing profile");
		}
		Profile profile = new Profile();
		profile.setName(createProfileReq.getName());
		profile.setDateOfBirth(createProfileReq.getDateOfBirth());
		profile.setPrimaryEmail(createProfileReq.getPrimaryEmail());
		profile.setSecondaryEmail(createProfileReq.getSecondaryEmail());
		profile.setAddress(createProfileReq.getAddress());
		profile.setCity(createProfileReq.getCity());
		profile.setState(createProfileReq.getState());
		profile.setCountry(createProfileReq.getCountry());
		profile.setPinCode(createProfileReq.getPinCode());
		profile.setContactNo(createProfileReq.getContactNo());

		Profile savedProfile = profileRepository.save(profile);

		ProfileLifecycle lifecycle = new ProfileLifecycle();
		lifecycle.setProfile(savedProfile);
		lifecycle.setDeleted(false);
		lifecycle.setDeletedAt(null);
		lifecycle.setRestoreAt(null);
		lifecycle.setLastUpdate("created");
		lifecycle.setDoneBy("admin");
		lifecycle.setLastUpdatedAt(LocalDateTime.now());
		profileLifecycleRepository.save(lifecycle);

		return savedProfile;
	}

	// GET PROFILE BY ID
	public Profile getProfileById(Long id) throws ProfileNotFoundException {
		return profileRepository.findById(id)
				.orElseThrow(() -> new ProfileNotFoundException("Profile Not Found with id : " + id));

	}

	@Transactional
	public Profile updateProfile(Long id, UpdateProfileReq updateProfileReq) throws ProfileNotFoundException {
		Profile existingProfile = profileRepository.findById(id)
				.orElseThrow(() -> new ProfileNotFoundException("Profile Not Found with id : " + id));

		// Merge DTO into existing profile
		profileMapper.updateProfileFromDto(updateProfileReq, existingProfile);

		ProfileLifecycle lifecycle = profileLifecycleRepository.findByProfileId(id)
				.orElseThrow(() -> new ProfileNotFoundException("ProfileLifecycle Not Found for id : " + id));

		lifecycle.setLastUpdate("updated");
		lifecycle.setDoneBy("admin"); // Dynamically set based on the user performing the update
		lifecycle.setLastUpdatedAt(LocalDateTime.now());

		profileLifecycleRepository.save(lifecycle);

		return profileRepository.save(existingProfile);
	}

	// DELETE PROFILE(Hard Delete)
	public void deleteProfile(Long id) throws ProfileNotFoundException {
		if (!profileRepository.existsById(id)) {
			throw new ProfileNotFoundException("Profile Not Found with id : " + id);
		}
		profileRepository.deleteById(id);
	}

	// 26/12
	public void softDeleteProfile(Long id) throws ProfileNotFoundException {
		ProfileLifecycle lifecycle = profileLifecycleRepository.findByProfileId(id)
				.orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id));

		lifecycle.setDeleted(true);
		lifecycle.setDeletedAt(LocalDateTime.now());
		lifecycle.setLastUpdate("deleted");
		lifecycle.setDoneBy("admin");
		lifecycle.setLastUpdatedAt(LocalDateTime.now());
		profileLifecycleRepository.save(lifecycle);
	}

	public void restoreProfile(Long id) throws ProfileNotFoundException {
		ProfileLifecycle lifecycle = profileLifecycleRepository.findByProfileId(id)
				.orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id));

		if (!lifecycle.isDeleted()) {
			throw new IllegalStateException("Profile is not marked as deleted.");
		}
		if (lifecycle.getDeletedAt().isAfter(LocalDateTime.now().minusMinutes(1))) {
			throw new RestoreWithinOneMinuteException("Cannot restore a profile within 1 minute of deletion.");
		}

		lifecycle.setDeleted(false);
		lifecycle.setRestoreAt(LocalDateTime.now());
		lifecycle.setLastUpdate("restored");
		lifecycle.setDoneBy("admin");
		lifecycle.setLastUpdatedAt(LocalDateTime.now());
		profileLifecycleRepository.save(lifecycle);
	}

	// tp get all deleted profile
	public List<Profile> getDeletedProfiles() {
		return profileLifecycleRepository.findAll().stream().filter(ProfileLifecycle::isDeleted)
				.map(ProfileLifecycle::getProfile).collect(Collectors.toList());
	}

	@Transactional
	public void purgeDeletedProfiles(Duration duration) {
		LocalDateTime cutoff = LocalDateTime.now().minus(duration);

		// Fetch profiles eligible for deletion
		List<ProfileLifecycle> profilesToPurge = profileLifecycleRepository.findAll().stream()
				.filter(lifecycle -> lifecycle.isDeleted() && lifecycle.getDeletedAt() != null
						&& lifecycle.getDeletedAt().isBefore(cutoff))
				.collect(Collectors.toList());

		// Delete associated profiles
		profilesToPurge.forEach(lifecycle -> {
			profileRepository.delete(lifecycle.getProfile());
		});

		// Delete profile lifecycles
		profileLifecycleRepository.deleteAll(profilesToPurge);
	}

	// this is for parmanent delete based on profile id if admin want to delete it
	// completely
	@Transactional
	public void parDeletedProfile(Long id) throws ProfileNotFoundException {
		ProfileLifecycle lifecycle = profileLifecycleRepository.findByProfileId(id)
				.orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id));

		Profile profile = lifecycle.getProfile();
		// First, delete the lifecycle entry
		profileLifecycleRepository.delete(lifecycle);
		// Then, delete the profile entry
		profileRepository.delete(profile);
	}

	// 06/01
	public ProfileLifecycle getProfileLifecycle(Long profileId) throws ProfileNotFoundException {
		ProfileLifecycle lifecycle = profileLifecycleRepository.findByProfileId(profileId).orElseThrow(
				() -> new ProfileNotFoundException("ProfileLifecycle Not Found for Profile ID: " + profileId));

		return lifecycle;
	}

//    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
//
//    public List<Profile> getProfilesWithUnapprovedStatus() {
//        List<Profile> allProfiles = profileRepository.findAll();
//        List<Profile> unapprovedProfiles = new ArrayList<>();
//
//        for (Profile profile : allProfiles) {
//            try {
//                // Fetch the ProfileStatus for each profile
//                ResponseEntity<ProfileStatusDto> profileStatusResponse = profileStatusFeignClient.getProfileStatusByProfileId(profile.getId());
//
//                // Check if the profile status is not approved
//                if (profileStatusResponse.getBody() != null && !profileStatusResponse.getBody().getIsApproved()) {
//                    unapprovedProfiles.add(profile);
//                }
//
//            } catch (Exception e) {
//                // Log the error (e.g., ProfileStatus not found or service unavailable)
//                logger.error("Error fetching profile status for profileId " + profile.getId(), e);
//            }
//        }
//
//        return unapprovedProfiles;
//    }

	public List<Map<String, Object>> getProfilesGroupedByCountry() {
		List<LocationResourceProjection> results = profileRepository.getProfilesGroupedByCountry();

		if (results.isEmpty()) {
			return new ArrayList<>(); // Return empty list instead of null
		}

		return results.stream().map(result -> {
			Map<String, Object> map = new HashMap<>();
			map.put("location", result.getLocation());
			map.put("resources", result.getResources());
			return map;
		}).collect(Collectors.toList());
	}

	// GET Distint country count
	public Map<String, Long> getDistinctCountryCount() {
		long count = profileRepository.getDistinctCountryCount();
		Map<String, Long> response = new HashMap<>();
		response.put("countryCount", count);
		return response;
	}

//	// Adding Role and Vertical
//	public ProfileRoleVertical addProfileRoleVertical(Long profileId, ProfileRoleVertical request) {
//		// Create new ProfileRoleVertical entry
//		ProfileRoleVertical newProfileRoleVertical = new ProfileRoleVertical();
//		newProfileRoleVertical.setProfileId(profileId); // Set profileId from URL
//		newProfileRoleVertical.setRoleId(request.getRoleId()); // Get roleId from body
//		newProfileRoleVertical.setVerticalId(request.getVerticalId()); // Get verticalId from body
//
//		return profileRoleAndVerticalRepository.save(newProfileRoleVertical);
//	}
//	
//	
//
//	// Update Role & Vertical by Profile ID
//	public Map<String, Long> updateProfileRoleVertical(Long profileId, Long roleId, Long verticalId) {
////        Optional<ProfileRoleVertical> existingRecord = roleAndVerticalRepository.findByProfileId(profileId);
//		Optional<ProfileRoleVertical> existingRecord = profileRoleAndVerticalRepository.findByProfileId(profileId);
//
//		if (existingRecord.isPresent()) {
//			ProfileRoleVertical profileRoleVertical = existingRecord.get();
//			profileRoleVertical.setRoleId(roleId);
//			profileRoleVertical.setVerticalId(verticalId);
//
//			profileRoleAndVerticalRepository.save(profileRoleVertical);
//
//			return Map.of("roleId", profileRoleVertical.getRoleId(), "verticalId", profileRoleVertical.getVerticalId());
//		} else {
//			throw new RuntimeException("Profile ID " + profileId + " not found.");
//		}
//	}
	
	
	public ProfileRoleVertical upsertProfileRoleVertical(Long profileId, ProfileRoleVertical request) {
	    Optional<ProfileRoleVertical> existingRecord = profileRoleAndVerticalRepository.findByProfileId(profileId);

	    ProfileRoleVertical profileRoleVertical;
	    if (existingRecord.isPresent()) {
	        // Update existing record
	        profileRoleVertical = existingRecord.get();
	        profileRoleVertical.setRoleId(request.getRoleId());
	        profileRoleVertical.setVerticalId(request.getVerticalId());
	    } else {
	        // Create new record
	        profileRoleVertical = new ProfileRoleVertical();
	        profileRoleVertical.setProfileId(profileId);
	        profileRoleVertical.setRoleId(request.getRoleId());
	        profileRoleVertical.setVerticalId(request.getVerticalId());
	    }

	  return  profileRoleAndVerticalRepository.save(profileRoleVertical);

	  // return 
	}

	
	
	
	
	
	
	

	public Page<Profile> getFilteredProfiles(Long roleId, Long verticalId, String country, Pageable pageable) {
		return profileRoleAndVerticalRepository.findProfilesByRoleVerticalAndCountry(roleId, verticalId, country,
				pageable);
	}

	public List<Role> getRolesByProfileId(Long profileId) {
		return profileRoleAndVerticalRepository.findRolesByProfileId(profileId);
	}

	public List<Vertical> getVerticalsByProfileId(Long profileId) {
		return profileRoleAndVerticalRepository.findVerticalsByProfileId(profileId);
	}
	
	public List<String> getUniqueCountries() {
        return profileRepository.getUniqueCountries();
    }
	

}
