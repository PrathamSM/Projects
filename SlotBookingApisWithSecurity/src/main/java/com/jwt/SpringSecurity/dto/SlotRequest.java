package com.jwt.SpringSecurity.dto;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SlotRequest {
    private Long userid;
    private LocalDate date;

    private DayOfWeek day;
    private LocalTime startTime;  // In LocalTime format
    private LocalTime endTime;    // In LocalTime format
}
