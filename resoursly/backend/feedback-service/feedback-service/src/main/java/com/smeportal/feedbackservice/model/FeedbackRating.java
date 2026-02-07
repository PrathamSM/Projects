package com.smeportal.feedbackservice.model;

import com.smeportal.feedbackservice.converter.Feedback2JsonConverter;
import com.smeportal.feedbackservice.converter.InterviewFeedbackJsonConverter;
import com.smeportal.feedbackservice.converter.PreferenceJsonConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class FeedbackRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long profileId;

    @NotNull(message = "Given By cannot be null")
    @Column(nullable = true)
    private Long givenBy;

    @Column(nullable = true)
    private String feedback;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    @Column(nullable = true)
    private Integer rating; // Rating between 1 and 5

    @Column(nullable = false)
    private LocalDateTime feedbackDate = LocalDateTime.now();

    @Convert(converter = PreferenceJsonConverter.class)
    private List<Preference> preferences;

    @Convert(converter = Feedback2JsonConverter.class)
    @Column(columnDefinition = "json" , nullable = true)
    private List<FeedbackDetail> feedbacks;

    @Convert(converter = InterviewFeedbackJsonConverter.class)
    @Column(columnDefinition = "json", nullable = true)
    private List<InterviewFeedback> interviewFeedbacks;

}