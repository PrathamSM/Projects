package com.smeportal.feedbackservice.dto;

import com.smeportal.feedbackservice.model.FeedbackDetail;
import com.smeportal.feedbackservice.model.InterviewFeedback;
import com.smeportal.feedbackservice.model.Preference;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateFeedbackReq {


    private Long givenBy;

    @Size(max = 1000, message = "Feedback cannot exceed 1000 characters")
    private String feedback;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;

    private List<Preference> preferences;
    private List<FeedbackDetail> feedbacks;
    private List<InterviewFeedback> interviewFeedbacks;

}
