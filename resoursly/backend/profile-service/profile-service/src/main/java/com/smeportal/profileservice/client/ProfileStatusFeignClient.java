package com.smeportal.profileservice.client;

import com.smeportal.profileservice.dto.ProfileStatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-status-service", url ="http://localhost:8085/profile-status")
public interface ProfileStatusFeignClient {
    @GetMapping("/{profileId}")
    ResponseEntity<ProfileStatusDto> getProfileStatusByProfileId(@PathVariable("profileId") Long profileId);
}
