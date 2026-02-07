package com.smeportal.searchms.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProfileStatus {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Boolean getNdaSigned() {
        return ndaSigned;
    }

    public void setNdaSigned(Boolean ndaSigned) {
        this.ndaSigned = ndaSigned;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    private Long profileId;
    private Boolean ndaSigned;

    @JsonProperty("availability")
    private Availability availability; // Nested Availability object

    @JsonProperty("isApproved")
    private Boolean isApproved;
}