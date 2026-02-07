package com.example.file_upload1.file_upload1.dto;


public class ProfileStatus {





    private Long profileId;


    private Boolean ndaSigned = false;

    // Availability as JSON
    private Availability availability;


    private Boolean isApproved = false;



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

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}
