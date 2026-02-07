package com.example.file_upload1.file_upload1.feign;

import com.example.file_upload1.file_upload1.dto.ProfessionalExperienceRequestDTO;
import com.example.file_upload1.file_upload1.dto.ProfessionalExperienceResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="professional-experience-service" , url = "localhost:8082/professional-experiences")
public interface ProfessionalExperienceClient {
    @PostMapping
    public ResponseEntity<ProfessionalExperienceResponseDTO> addExperience(@RequestBody ProfessionalExperienceRequestDTO request) ;

}
