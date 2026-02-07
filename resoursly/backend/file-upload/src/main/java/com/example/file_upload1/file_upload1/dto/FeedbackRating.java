package com.example.file_upload1.file_upload1.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

public class FeedbackRating {

    private Long profileId;
    private Long givenBy;
    private String feedback;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating; // Rating between 1 and 5

    private LocalDateTime feedbackDate = LocalDateTime.now();



    // Getter and Setter for profileId
    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    // Getter and Setter for givenBy
    public Long getGivenBy() {
        return givenBy;
    }

    public void setGivenBy(Long givenBy) {
        this.givenBy = givenBy;
    }

    // Getter and Setter for feedback
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    // Getter and Setter for rating
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    // Getter and Setter for feedbackDate
    public LocalDateTime getFeedbackDate(LocalDateTime now) {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDateTime feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
}