package com.smeproject.manage_project_service.service;

import com.smeproject.manage_project_service.dto.*;
import com.smeproject.manage_project_service.feign.*;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ManageProjectService {

    @Autowired
    private ProjectsFeignClient projectsFeignClient;

    @Autowired
    private SMERequirementsFeignClient smeRequirementsFeignClient;

    @Autowired
    private ProjectTeamFeignClient projectTeamFeignClient;

    @Autowired
    private AssignResourceClient assignResourceClient;


    @Autowired
    private DocumentUploadFeignClient documentUploadFeignClient;

    @Autowired
    private ManageProjectMapper manageProjectMapper;


    public ManageProjectDTO getProjectDetails(String projectId) {
        String projectName;
        LocalDate projectStartDate;
        LocalDate projectEndDate;
        Double budget;
        String currency;
        Integer numberOfSMEsRequired;
        String managerName;
        String managerEmail;

        Integer profileCount;
        String firstSkill;
        String contactName;

        try {
            projectName = projectsFeignClient.getProjectName(projectId);
        } catch (FeignException e) {
            projectName = "NA";
        }

        try {
            projectStartDate = projectsFeignClient.getProjectStartDate(projectId);
        } catch (FeignException e) {
            projectStartDate = null; // Dates can be null if unavailable
        }

        try {
            projectEndDate = projectsFeignClient.getProjectEndDate(projectId);
        } catch (FeignException e) {
            projectEndDate = null;
        }

        try {
            budget = projectsFeignClient.getPlannedBudget(projectId);
        } catch (FeignException e) {
            budget = null;
        }

        try {
            currency = projectsFeignClient.getCurrency(projectId); // Fetching currency
        } catch (FeignException e) {
            currency = "NA"; // Default to "NA" if not found
        }

        try {
            numberOfSMEsRequired = smeRequirementsFeignClient.getNumberOfSMEs(projectId);
        } catch (FeignException e) {
            numberOfSMEsRequired = null;
        }

        try {
            managerName = projectTeamFeignClient.getManagerName(projectId);
        } catch (FeignException e) {
            managerName = "NA";
        }

        try {
            managerEmail = projectTeamFeignClient.getManagerEmail(projectId);
        } catch (FeignException e) {
            managerEmail = "NA";
        }


        try {
            profileCount = assignResourceClient.getProfileCountByProjectId(projectId).getBody();
        } catch (FeignException e) {
            profileCount = 0;
        }

        try {
            firstSkill = smeRequirementsFeignClient.getFirstRequiredSkill(projectId);
        } catch (FeignException e) {
            firstSkill = "NA";
        }

        try {
            contactName = projectsFeignClient.getProjectContactPerson(projectId);
        } catch (FeignException e) {
            contactName = "NA";
        }


        return new ManageProjectDTO(
                projectId,
                projectName,
                projectStartDate,
                projectEndDate,
                budget,
                currency,
                numberOfSMEsRequired,
                managerName,
                managerEmail,
                profileCount,
                firstSkill,
                contactName

        );
    }


    public String deleteAllDataByProjectId(String projectId) {
        StringBuilder responseMessage = new StringBuilder();

        try {
            documentUploadFeignClient.deleteDocuments(projectId);
            responseMessage.append("Documents deleted successfully. ");
        } catch (FeignException e) {
            responseMessage.append("Failed to delete documents. ");
        }

        try {
            projectTeamFeignClient.deleteProjectTeam(projectId);
            responseMessage.append("Project team deleted successfully. ");
        } catch (FeignException e) {
            responseMessage.append("Failed to delete project team. ");
        }

        try {
            smeRequirementsFeignClient.deleteSMERequirements(projectId);
            responseMessage.append("SME requirements deleted successfully. ");
        } catch (FeignException e) {
            responseMessage.append("Failed to delete SME requirements. ");
        }

        try {
            projectsFeignClient.deleteProject(projectId);
            responseMessage.append("Project deleted successfully.");
        } catch (FeignException e) {
            responseMessage.append("Failed to delete project.");
        }

        return responseMessage.toString();
    }

//    public String updateProjectDetails(String projectId, ManageProjectUpdateDTO updateDTO) {
//        // Fetch existing values
//        String projectName = projectsFeignClient.getProjectName(projectId);
//        LocalDate startDate = projectsFeignClient.getProjectStartDate(projectId);
//        LocalDate endDate = projectsFeignClient.getProjectEndDate(projectId);
//        Double budget = projectsFeignClient.getPlannedBudget(projectId);
//        String currency = projectsFeignClient.getCurrency(projectId);
//        Integer numberOfSMEs = smeRequirementsFeignClient.getNumberOfSMEs(projectId);
//        String managerName = projectTeamFeignClient.getManagerName(projectId);
//        String managerEmail = projectTeamFeignClient.getManagerEmail(projectId);
//
//        // Use provided values, fallback to existing ones if null
//        ManageProjectDTO updatedProject = new ManageProjectDTO(
//                projectId,
//                updateDTO.getProjectName() != null ? updateDTO.getProjectName() : projectName,
//                updateDTO.getProjectStartDate() != null ? updateDTO.getProjectStartDate() : startDate,
//                updateDTO.getProjectEndDate() != null ? updateDTO.getProjectEndDate() : endDate,
//                updateDTO.getBudget() != null ? updateDTO.getBudget() : budget,
//                updateDTO.getCurrency() != null ? updateDTO.getCurrency() : currency,
//                updateDTO.getNumberOfSMEsRequired() != null ? updateDTO.getNumberOfSMEsRequired() : numberOfSMEs,
//                updateDTO.getManagerName() != null ? updateDTO.getManagerName() : managerName,
//                updateDTO.getManagerEmail() != null ? updateDTO.getManagerEmail() : managerEmail
//        );

//        // Call existing PUT API to update the project
//        projectsFeignClient.updateProject(projectId, updatedProject);
//        return "Project details updated successfully!";
//    }

    public List<ProjectCombinedDTO> getAllProjectDetails() {
        List<ProjectDetailsDTO> projectDetails = projectsFeignClient.getAllProjectDetails();
        List<ProjectSMEDetailsDTO> smeDetails = smeRequirementsFeignClient.getProjectSMEDetails();
        List<Map<String, Object>> profileCounts = assignResourceClient.getProjectProfileCount();

        Map<String, ProjectSMEDetailsDTO> smeMap = smeDetails.stream()
                .collect(Collectors.toMap(ProjectSMEDetailsDTO::getProjectId, dto -> dto));

        Map<String, Integer> profileCountMap = profileCounts.stream()
                .collect(Collectors.toMap(
                        data -> data.get("projectId").toString(),
                        data -> Integer.parseInt(data.get("profileCount").toString())
                ));

        return projectDetails.stream().map(project -> {
            ProjectSMEDetailsDTO sme = smeMap.getOrDefault(project.getProjectId(), new ProjectSMEDetailsDTO());
            int profileCount = profileCountMap.getOrDefault(project.getProjectId(), 0);

            return new ProjectCombinedDTO(
                    project.getProjectId(),
                    project.getProjectName(),
                    project.getProjectStartDate(),
                    project.getProjectEndDate(),
                    project.getProjectContactPerson(),
                    project.getProjectLocation(),
                    project.getPlannedBudget(),
                    sme.getNumberOfSMEs(),
                    sme.getFirstRequiredSkill(),
                    profileCount
            );
        }).collect(Collectors.toList());
    }



}
