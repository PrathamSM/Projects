package com.smeportal.project_team_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProjectTeamUpdateDTO {
    private List<String> projectManagerNames;
    private List<String> projectManagerEmails;
    private List<String> projectManagerPhones;
    private List<String> smeNames;
    private List<String> smeEmails;
    private List<String> smePhones;
    private String pocName;
    private String pocEmail;
    private String pocPhone;
}
