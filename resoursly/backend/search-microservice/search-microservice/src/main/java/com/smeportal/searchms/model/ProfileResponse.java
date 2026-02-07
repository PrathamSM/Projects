package com.smeportal.searchms.model;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


public class ProfileResponse {
    private Long id;
    private String name;
    private String dateOfBirth;
    private String primaryEmail;
    private String secondaryEmail;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private String contactNo;
    private ProfileStatus profileStatus;
    private FeedbackRating feedbackRating;
    private ProfileLifecycle profileLifecycle;
    private ProfessionalExperienceResponseDTO professionalExperience;
    private List<Role> role;
    private List<Vertical> vertical;

    public List<Role> getRole() {
		return role;
	}

	public void setRole(List<Role> role) {
		this.role = role;
	}

	public List<Vertical> getVertical() {
		return vertical;
	}

	public void setVertical(List<Vertical> vertical) {
		this.vertical = vertical;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public ProfileStatus getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }

    public FeedbackRating getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(FeedbackRating feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public ProfileLifecycle getProfileLifecycle() {
        return profileLifecycle;
    }

    public void setProfileLifecycle(ProfileLifecycle profileLifecycle) {
        this.profileLifecycle = profileLifecycle;
    }

    public ProfessionalExperienceResponseDTO getProfessionalExperience() {
        return professionalExperience;
    }

    public void setProfessionalExperience(ProfessionalExperienceResponseDTO professionalExperience) {
        this.professionalExperience = professionalExperience;
    }

    @Data
    public static class ProfileLifecycle {
        private Long id;
        private boolean deleted;
        private String deletedAt;
        private String restoreAt;

        public String getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(String lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public String getDoneBy() {
            return DoneBy;
        }

        public void setDoneBy(String doneBy) {
            DoneBy = doneBy;
        }

        public LocalDateTime getLastUpdatedAt() {
            return lastUpdatedAt;
        }

        public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
            this.lastUpdatedAt = lastUpdatedAt;
        }

        //06/01
        private String lastUpdate;
        private String DoneBy;
        private LocalDateTime lastUpdatedAt;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getRestoreAt() {
            return restoreAt;
        }

        public void setRestoreAt(String restoreAt) {
            this.restoreAt = restoreAt;
        }
    }
}