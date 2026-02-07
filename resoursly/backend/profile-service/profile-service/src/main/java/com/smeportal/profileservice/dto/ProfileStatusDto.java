package com.smeportal.profileservice.dto;

import lombok.Data;

@Data
public class ProfileStatusDto {
    private Long profileId;
    private Boolean isApproved;
}
