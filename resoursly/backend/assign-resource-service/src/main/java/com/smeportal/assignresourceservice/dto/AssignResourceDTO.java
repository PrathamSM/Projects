package com.smeportal.assignresourceservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignResourceDTO {
    private Long profileId;
    private String projectId;

    private String clientName;

    private boolean assigned;

    private String projectName;

    private boolean dedicated;
}
