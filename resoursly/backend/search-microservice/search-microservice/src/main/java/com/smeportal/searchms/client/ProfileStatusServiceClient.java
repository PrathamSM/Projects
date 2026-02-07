package com.smeportal.searchms.client;

import com.smeportal.searchms.model.ProfileStatus;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-status-service", url = "${profile-status-service.url}")
public interface ProfileStatusServiceClient {

    @GetMapping("/{pId}")
    ProfileStatus getProfileStatusByProfileId(@PathVariable Long pId);

    @GetMapping("/unapproved-ids")
    public List<Long> getUnapprovedProfileIds();
}
