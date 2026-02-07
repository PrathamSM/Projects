package com.smeportal.feedbackservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Preference {
    private String platform;
    private String url;

    // Default constructor
    public Preference() {}

    // Parameterized constructor
    public Preference(String platform, String url) {
        this.platform = platform;
        this.url = url;
    }
}