package com.smeportal.sme_requirements_service.service;

import com.smeportal.sme_requirements_service.dto.ProjectSMEDetailsDTO;
import com.smeportal.sme_requirements_service.dto.SMERequirementsUpdateDTO;
import com.smeportal.sme_requirements_service.entity.SMERequirements;
import com.smeportal.sme_requirements_service.feign.ProjectServiceClient;

import com.smeportal.sme_requirements_service.repository.SMERequirementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SMERequirementsService {

    @Autowired
    private SMERequirementsRepository repository;

    @Autowired
    private ProjectServiceClient projectServiceClient;

    @Autowired
    private SMERequirementsMapper mapper;

    public SMERequirements createSMERequirement(SMERequirements request) {
        Boolean isProjectValid = projectServiceClient.validateProjectId(request.getProjectId());

        if (Boolean.FALSE.equals(isProjectValid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid projectId: " + request.getProjectId() + ". Project does not exist.");
        }

        return repository.save(request);
    }

    public Optional<SMERequirements> getRequirementById(Long id) {
        return repository.findById(id);
    }

    public String deleteSMERequirement(Long id) {
        Optional<SMERequirements> existingRequirement = repository.findById(id);

        if (existingRequirement.isPresent()) {
            repository.deleteById(id);
            return "SME Requirement with ID " + id + " deleted successfully.";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SME Requirement with ID " + id + " not found.");
        }
    }

    public SMERequirements updateSMERequirement(Long id, SMERequirementsUpdateDTO updateDTO) {
        SMERequirements existingRequirement = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SME Requirement with ID " + id + " not found."));

        mapper.updateSMERequirementFromDto(updateDTO, existingRequirement);

        return repository.save(existingRequirement);
    }

//    public Integer getNumberOfSMEsByProjectId(String projectId) {
//        return repository.findByProjectId(projectId)
//                .map(SMERequirements::getNumberOfSMEs)
//                .orElse(0); // If not found, return 0
//    }

    public Integer getNumberOfSMEsByProjectId(String projectId) {
        Integer numberOfSMEs = repository.findNumberOfSMEsByProjectId(projectId);
        return (numberOfSMEs != null) ? numberOfSMEs : 0;
    }

    public SMERequirements updateSMERequirementByProjectId(String projectId, SMERequirementsUpdateDTO updateDTO) {
        SMERequirements existingRequirement = repository.findByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SME Requirement for Project ID " + projectId + " not found."));

        mapper.updateSMERequirementFromDto(updateDTO, existingRequirement);

        return repository.save(existingRequirement);
    }

    public String deleteSMERequirementByProjectId(String projectId) {
        Optional<SMERequirements> existingRequirement = repository.findByProjectId(projectId);

        if (existingRequirement.isPresent()) {
            repository.delete(existingRequirement.get());
            return "SME Requirement for Project ID " + projectId + " deleted successfully.";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SME Requirement for Project ID " + projectId + " not found.");
        }
    }

    // 06-03

    public String getFirstRequiredSkillByProjectId(String projectId) {
        SMERequirements requirement = repository.findByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SME Requirement for Project ID " + projectId + " not found."));

        List<String> skills = requirement.getRequiredSkills();

        if (skills == null || skills.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No required skills found for Project ID " + projectId);
        }

        return skills.get(0);
    }

    public List<SMERequirements> getAllSMERequirements() {

        return repository.findAll();
    }


    // 07-03

    public List<ProjectSMEDetailsDTO> getProjectSMEDetails() {
        List<SMERequirements> allSMERequirements = repository.findAll();
        List<ProjectSMEDetailsDTO> projectSMEDetails = new ArrayList<>();

        for (SMERequirements requirement : allSMERequirements) {
            ProjectSMEDetailsDTO details = new ProjectSMEDetailsDTO();
            details.setProjectId(requirement.getProjectId());
            details.setNumberOfSMEs(requirement.getNumberOfSMEs());

            // 0th index
            List<String> requiredSkills = requirement.getRequiredSkills();
            if (requiredSkills != null && !requiredSkills.isEmpty()) {
                details.setFirstRequiredSkill(requiredSkills.get(0));
            } else {
                details.setFirstRequiredSkill("No skills available");
            }
            projectSMEDetails.add(details);
        }

        return projectSMEDetails;
    }

}
