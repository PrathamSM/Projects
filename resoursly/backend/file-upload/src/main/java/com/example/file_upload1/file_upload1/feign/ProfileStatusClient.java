package com.example.file_upload1.file_upload1.feign;


import com.example.file_upload1.file_upload1.dto.ProfileStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name= "profile-status-service",url= "localhost:8085/profile-status")

public interface ProfileStatusClient {

    @PostMapping
    public ResponseEntity<?> createProfileStatus(@RequestBody ProfileStatus profileStatus);
}
