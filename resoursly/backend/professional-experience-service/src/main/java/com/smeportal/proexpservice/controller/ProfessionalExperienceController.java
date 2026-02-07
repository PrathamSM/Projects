package com.smeportal.proexpservice.controller;

import com.smeportal.proexpservice.dto.ProfessionalExperienceRequestDTO;
import com.smeportal.proexpservice.dto.ProfessionalExperienceResponseDTO;
import com.smeportal.proexpservice.service.ProfessionalExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professional-experiences")
@CrossOrigin("http://localhost:5173")
public class ProfessionalExperienceController {

    @Autowired
    private ProfessionalExperienceService service;

    @PostMapping
    public ResponseEntity<ProfessionalExperienceResponseDTO> addExperience(@RequestBody ProfessionalExperienceRequestDTO request) {
        ProfessionalExperienceResponseDTO response = service.addProfessionalExperience(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalExperienceResponseDTO> getExperience(@PathVariable Long id) {
        ProfessionalExperienceResponseDTO response = service.getProfessionalExperience(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        service.deleteProfessionalExperience(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalExperienceResponseDTO> updateExperience(@PathVariable Long id,
                                                                              @RequestBody ProfessionalExperienceRequestDTO request) {
        ProfessionalExperienceResponseDTO response = service.updateProfessionalExperience(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

