package com.example.file_upload1.file_upload1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

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

    // Getter for platform
    public String getPlatform() {
        return platform;
    }

    // Setter for platform
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    // Getter for url
    public String getUrl() {
        return url;
    }

    // Setter for url
    public void setUrl(String url) {
        this.url = url;
    }
}
