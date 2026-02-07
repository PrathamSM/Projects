package com.smeportal.proexpservice.mapper;

import com.smeportal.proexpservice.dto.ProfessionalExperienceRequestDTO;
import com.smeportal.proexpservice.dto.ProfessionalExperienceResponseDTO;
import com.smeportal.proexpservice.model.ProfessionalExperience;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProfessionalExperienceMapper {

    // Map from Entity to Response DTO
    @Mapping(target = "primaryDisciplines", source = "primaryDisciplineIds")
   @Mapping(target = "secondaryDisciplines", source = "secondaryDisciplineIds")
    ProfessionalExperienceResponseDTO toResponseDTO(ProfessionalExperience entity);

    // Map from Request DTO to Entity
   @Mapping(target = "primaryDisciplineIds", source = "primaryDisciplines")
   @Mapping(target = "secondaryDisciplineIds", source = "secondaryDisciplines")
    ProfessionalExperience toEntity(ProfessionalExperienceRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfileExperienceFromDto(ProfessionalExperienceRequestDTO dto, @MappingTarget ProfessionalExperience entity);

}