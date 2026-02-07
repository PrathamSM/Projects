package com.smeportal.project_team_service.repository;

import com.smeportal.project_team_service.entity.ProjectTeam;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectTeamRepository extends JpaRepository<ProjectTeam, Long> {
    Optional<ProjectTeam> findByProjectId(String projectId);
    void deleteByProjectId(String projectId);

}

