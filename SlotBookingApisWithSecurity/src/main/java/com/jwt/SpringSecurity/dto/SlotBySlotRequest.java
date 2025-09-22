package com.jwt.SpringSecurity.dto;

import lombok.Data;

@Data
public class SlotBySlotRequest {
    private Long slotid;
    private String username;
    private String date;
    private String day;
    private String startTime;
    private String endTime;
    private String status;

}