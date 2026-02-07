package com.smeportal.searchms.client;

import com.smeportal.searchms.model.ProfileResponse;
import com.smeportal.searchms.model.Role;
import com.smeportal.searchms.model.Vertical;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

import java.util.List;

@FeignClient(name = "profile-service", url = "${profile-service.url}")
public interface ProfileServiceClient {
    @GetMapping
    public List<ProfileResponse> getAllProfiles();

    @GetMapping("/{id}")
    ProfileResponse getProfileById(@PathVariable Long id);

    @GetMapping("/page")
    Page<ProfileResponse> getAllProfilesByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @GetMapping("/count")
    public String getProfileCount();
    
    @GetMapping("/filtered")
    public Page<ProfileResponse> getProfiles(
        @RequestParam(required = false) Long roleId,
        @RequestParam(required = false) Long verticalId,
        @RequestParam(required = false) String country,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    );
 
    @GetMapping("/roles/{profileId}")
    public List<Role> getRolesByProfileId(@PathVariable Long profileId);
 
    @GetMapping("/verticals/{profileId}")
    public List<Vertical> getVerticalsByProfileId(@PathVariable Long profileId) ;
    
}
