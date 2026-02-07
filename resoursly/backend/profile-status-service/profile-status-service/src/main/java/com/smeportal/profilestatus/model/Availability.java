package com.smeportal.profilestatus.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Availability {
    private LocalDate from;
    private LocalDate to;
    private Boolean isAvailable;

    // Getters and Setters

    public Availability() {}
}
