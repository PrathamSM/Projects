package com.smeportal.sme_requirements_service.service;


import com.smeportal.sme_requirements_service.dto.SMERequirementsUpdateDTO;
import com.smeportal.sme_requirements_service.entity.SMERequirements;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SMERequirementsMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSMERequirementFromDto(SMERequirementsUpdateDTO dto, @MappingTarget SMERequirements entity);
}