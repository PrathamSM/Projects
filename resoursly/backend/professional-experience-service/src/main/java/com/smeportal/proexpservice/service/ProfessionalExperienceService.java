package com.smeportal.proexpservice.service;

import com.smeportal.proexpservice.dto.DisciplineDTO;
import com.smeportal.proexpservice.dto.ProfessionalExperienceRequestDTO;
import com.smeportal.proexpservice.dto.ProfessionalExperienceResponseDTO;
import com.smeportal.proexpservice.feign.DisciplineFeignClient;
import com.smeportal.proexpservice.mapper.ProfessionalExperienceMapper;
import com.smeportal.proexpservice.model.ProfessionalExperience;
import com.smeportal.proexpservice.repository.ProfessionalExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProfessionalExperienceService {

    @Autowired
    private ProfessionalExperienceRepository repository;

    @Autowired
    private DisciplineFeignClient disciplineClient;

    @Autowired
    private ProfessionalExperienceMapper professionalExperienceMapper;





    public ProfessionalExperienceResponseDTO addProfessionalExperience(ProfessionalExperienceRequestDTO request) {
        if (request.getPrimaryDisciplines() == null || request.getSecondaryDisciplines() == null) {
            throw new IllegalArgumentException("Discipline names cannot be null");
        }

        // Save new disciplines and log results (if needed)
        List<DisciplineDTO> savedPrimaryDisciplines = disciplineClient.saveNewDisciplines(request.getPrimaryDisciplines()).getBody();
        List<DisciplineDTO> savedSecondaryDisciplines = disciplineClient.saveNewDisciplines(request.getSecondaryDisciplines()).getBody();

        // Fetch IDs for primary and secondary disciplines
        List<Long> primaryIds = disciplineClient.getDisciplineIdsByName(request.getPrimaryDisciplines()).getBody();
        List<Long> secondaryIds = disciplineClient.getDisciplineIdsByName(request.getSecondaryDisciplines()).getBody();

        if (primaryIds == null || secondaryIds == null) {
            throw new RuntimeException("Failed to fetch discipline IDs from Discipline Service");
        }

        // Map DTO to Entity
        ProfessionalExperience experience = new ProfessionalExperience();
        experience.setProfileId(request.getProfileId());
        experience.setPrimaryDisciplineIds(primaryIds);
        experience.setSecondaryDisciplineIds(secondaryIds);
        experience.setProfession(request.getProfession());
        experience.setQualification(request.getQualification());
        experience.setExperienceYears(request.getExperienceYears());
        experience.setRelevantExperience(request.getRelevantExperience());

        // Save entity to database
        ProfessionalExperience savedExperience = repository.save(experience);

        return mapToResponseDTO(savedExperience);
    }

    private ProfessionalExperienceResponseDTO mapToResponseDTO(ProfessionalExperience experience) {
        List<String> primaryNames = disciplineClient.getDisciplineNamesByIds(experience.getPrimaryDisciplineIds()).getBody();
        List<String> secondaryNames = disciplineClient.getDisciplineNamesByIds(experience.getSecondaryDisciplineIds()).getBody();

        ProfessionalExperienceResponseDTO response = new ProfessionalExperienceResponseDTO();
        response.setId(experience.getId());
        response.setProfileId(experience.getProfileId());
        response.setPrimaryDisciplines(primaryNames);
        response.setSecondaryDisciplines(secondaryNames);
        response.setProfession(experience.getProfession());
        response.setQualification(experience.getQualification());
        response.setExperienceYears(experience.getExperienceYears());
        response.setRelevantExperience(experience.getRelevantExperience());

        return response;
    }


    public ProfessionalExperienceResponseDTO getProfessionalExperience(Long id) {
        // Fetch the ProfessionalExperience entity by Profile ID
        ProfessionalExperience experience = repository.findByProfileId(id)
                .orElseThrow(() -> new RuntimeException("Professional Experience not found for Profile ID: " + id));

        // Map the entity to the response DTO
        return mapToResponseDTO(experience);
    }


    public void deleteProfessionalExperience(Long id) {
        ProfessionalExperience existingExperience = repository.findById(id).orElseThrow(()->new RuntimeException("not found"));
        repository.delete(existingExperience);
    }

    public ProfessionalExperienceResponseDTO updateProfessionalExperience(Long id, ProfessionalExperienceRequestDTO request) {
        // Fetch the existing professional experience
        ProfessionalExperience existingExperience = repository.findByProfileId(id)
                .orElseThrow(() -> new RuntimeException("ProfessionalExperience not found"));

        // Use MapStruct to update the entity
        professionalExperienceMapper.updateProfileExperienceFromDto(request, existingExperience);

        //  DisciplineDTO disciplineDTO  = new DisciplineDTO();

        if (request.getPrimaryDisciplines() != null && !request.getPrimaryDisciplines().isEmpty()) {
            // Save new primary disciplines and fetch their IDs
            List<DisciplineDTO> savedPrimaryDisciplines = disciplineClient.saveNewDisciplines(request.getPrimaryDisciplines()).getBody();
            List<Long> primaryIds = disciplineClient.getDisciplineIdsByName(request.getPrimaryDisciplines()).getBody();
            existingExperience.setPrimaryDisciplineIds(primaryIds);
        }

        if (request.getSecondaryDisciplines() != null && !request.getSecondaryDisciplines().isEmpty()) {
            // Save new secondary disciplines and fetch their IDs
            List<DisciplineDTO> savedSecondaryDisciplines = disciplineClient.saveNewDisciplines(request.getSecondaryDisciplines()).getBody();
            List<Long> secondaryIds = disciplineClient.getDisciplineIdsByName(request.getSecondaryDisciplines()).getBody();
            existingExperience.setSecondaryDisciplineIds(secondaryIds);
        }
        if(request.getSecondaryDisciplines() == null || request.getSecondaryDisciplines().isEmpty()) {
            existingExperience.setSecondaryDisciplineIds(Collections.emptyList());
        }
        ProfessionalExperience updatedExperience = repository.save(existingExperience);
        // Map the updated entity to the response DTO
        return professionalExperienceMapper.toResponseDTO(updatedExperience);
    }
}
