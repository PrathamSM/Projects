package com.smeportal.project_team_service.service;

import com.smeportal.project_team_service.dto.ProjectTeamUpdateDTO;
import com.smeportal.project_team_service.entity.ProjectTeam;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProjectTeamMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProjectTeamFromDto(ProjectTeamUpdateDTO dto, @MappingTarget ProjectTeam entity);
}
