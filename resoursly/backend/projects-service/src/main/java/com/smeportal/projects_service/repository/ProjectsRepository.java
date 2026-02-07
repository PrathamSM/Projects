package com.smeportal.projects_service.repository;

import com.smeportal.projects_service.dto.ProjectIdNameDTO;
import com.smeportal.projects_service.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects, String> {

    @Query("SELECT MAX(p.projectId) FROM Projects p") //it is used for getting the last project id
    String findLastProjectId();

    boolean existsByProjectId(String projectId);

    @Query("SELECT new com.smeportal.projects_service.dto.ProjectIdNameDTO(p.projectId, p.projectName) FROM Projects p")
    List<ProjectIdNameDTO> findAllProjectIdsAndNames();
}
