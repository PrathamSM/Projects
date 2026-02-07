package com.smeportal.profilestatus.service;

import com.smeportal.profilestatus.dto.ProfileStatusDto;
import com.smeportal.profilestatus.dto.ProfileStatusResponseDto;
import com.smeportal.profilestatus.exception.ProfileNotFoundException;
import com.smeportal.profilestatus.model.ProfileStatus;
import com.smeportal.profilestatus.repository.ProfileStatusMapper;
import com.smeportal.profilestatus.repository.ProfileStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileStatusService {


    @Autowired
    private ProfileStatusRepository profileStatusRepository;

    @Autowired
    private ProfileStatusMapper profileStatusMapper;

    //CREATE PROFILE STATUS
    public ProfileStatus createProfileStatus(ProfileStatus profileStatus) {
        return profileStatusRepository.save(profileStatus);
    }


    //GET PROFILE STATUS BY PROFILE ID
    public ProfileStatus getProfileStatusByProfileId(Long pId) {
        return profileStatusRepository.findByProfileId(pId).orElseThrow(
                ()-> new ProfileNotFoundException("Profile Not Found With ID : " + pId)
        );
    }

    // UPDATE PROFILE STATUS
    public ProfileStatus updateProfileStatus(Long pId, ProfileStatusDto profileStatusDto) {
        ProfileStatus existingStatus = profileStatusRepository.findByProfileId(pId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile Not Found With ID : " + pId));

        if(profileStatusDto.getNdaSigned() != null) {
            existingStatus.setNdaSigned(profileStatusDto.getNdaSigned());
        }
        // Log incoming request data for debugging
        System.out.println("Incoming isApproved: " + profileStatusDto.getIsApproved());

        // Explicitly update isApproved if it's present in the request
        if (profileStatusDto.getIsApproved() != null) {
            existingStatus.setIsApproved(profileStatusDto.getIsApproved());
        } else {
            System.out.println("isApproved value is null, keeping existing value: " + existingStatus.getIsApproved());
        }

        // Update other fields using the mapper
        profileStatusMapper.updateProfileStatusFromDto(profileStatusDto, existingStatus);

        System.out.println("Updated isApproved: " + existingStatus.getIsApproved());

        return profileStatusRepository.save(existingStatus);
    }

    public void deleteProfileStatus(Long pId) {
        ProfileStatus profileStatus = profileStatusRepository.findByProfileId(pId).orElseThrow(
                ()-> new ProfileNotFoundException("Profile Not Found With ID : " + pId)
        );

        profileStatusRepository.deleteById(profileStatus.getId());
    }

    public List<ProfileStatusResponseDto> getProfilesWithNotApprovedStatus() {
        return profileStatusRepository.findByIsApprovedFalse()
                .stream()
                .map(profile -> new ProfileStatusResponseDto( profile.getProfileId()))
                .collect(Collectors.toList());
    }

    public int getNdaSignedCount() {
        return profileStatusRepository.countByNdaSignedTrue();
    }

    public List<Long> getUnapprovedProfileIds() {
        return profileStatusRepository.findUnapprovedProfileIds();
    }

    public int getTotal(){
        return  profileStatusRepository.countTotal();
    }
}
