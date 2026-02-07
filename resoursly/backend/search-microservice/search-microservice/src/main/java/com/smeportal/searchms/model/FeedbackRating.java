package com.smeportal.searchms.model;


import lombok.Data;

@Data
public class FeedbackRating {
    private Long id;
    private Long givenBy;
    private String feedback;
    private int rating;
    private String feedbackDate;

    public Long getId() {
        return id;
    }

    public Long getGivenBy() {
        return givenBy;
    }

    public String getFeedback() {
        return feedback;
    }

    public int getRating() {
        return rating;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGivenBy(Long givenBy) {
        this.givenBy = givenBy;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setFeedbackDate(String feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
}
