package com.smeportal.projects_service.service;

import com.smeportal.projects_service.dto.ProjectUpdateDTO;
import com.smeportal.projects_service.entity.Projects;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {
    void updateProjectFromDto(ProjectUpdateDTO dto, @MappingTarget Projects entity);
}
