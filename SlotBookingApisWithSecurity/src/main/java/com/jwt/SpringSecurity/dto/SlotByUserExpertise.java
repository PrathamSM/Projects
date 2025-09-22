package com.jwt.SpringSecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class SlotByUserExpertise {

    // User details
    private Long userid;
    private String username;
    private String email;
    private String expertise;
    private String position;

    // Slot details
    private Long slotid;
    private LocalDate date;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
}