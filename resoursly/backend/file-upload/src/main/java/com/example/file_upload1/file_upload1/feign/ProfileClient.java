package com.example.file_upload1.file_upload1.feign;




import com.example.file_upload1.file_upload1.dto.CreateProfileReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "http://localhost:8081/profiles")  // Adjust URL if needed
public interface ProfileClient {

    @PostMapping
    ResponseEntity<?> createProfile(@RequestBody CreateProfileReq createProfileReq);
}
