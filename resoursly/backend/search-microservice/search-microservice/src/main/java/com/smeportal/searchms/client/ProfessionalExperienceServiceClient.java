package com.smeportal.searchms.client;

import com.smeportal.searchms.model.ProfessionalExperienceResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "professional-experience-service", url = "${professional-experience-service.url}")
public interface ProfessionalExperienceServiceClient {

    @GetMapping("/{pId}")
    ProfessionalExperienceResponseDTO getProfessionalExperience(@PathVariable Long pId);

}
