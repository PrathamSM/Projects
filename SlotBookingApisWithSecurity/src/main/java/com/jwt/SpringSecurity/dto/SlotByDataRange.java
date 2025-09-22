package com.jwt.SpringSecurity.dto;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SlotByDataRange {
    private Long userid;
    private LocalDate startdate;  // Start day of the range
    private LocalDate enddate;    // End day of the range
    private LocalTime starttime;  // Start time for each day
    private LocalTime endtime;    // End time for each day
    private DayOfWeek day;        // Can be set to the first day of the range or specific days
}