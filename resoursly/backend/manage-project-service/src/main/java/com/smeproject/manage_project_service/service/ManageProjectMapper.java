package com.smeproject.manage_project_service.service;

import com.smeproject.manage_project_service.dto.ManageProjectUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ManageProjectMapper {
    @Mapping(target = "projectName", source = "dto.projectName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "projectStartDate", source = "dto.projectStartDate", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "projectEndDate", source = "dto.projectEndDate", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "budget", source = "dto.budget", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "numberOfSMEsRequired", source = "dto.numberOfSMEsRequired", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "managerName", source = "dto.managerName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "managerEmail", source = "dto.managerEmail", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ManageProjectUpdateDTO updateFields(ManageProjectUpdateDTO dto, @MappingTarget ManageProjectUpdateDTO existingData);
}
