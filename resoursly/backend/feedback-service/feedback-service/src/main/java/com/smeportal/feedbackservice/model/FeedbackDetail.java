package com.smeportal.feedbackservice.model;
 
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
 
import java.time.LocalDate;
 
@Data
public class FeedbackDetail {
 
    private String projectName;
 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
 
    private int ratings;
    private String review;
    private String givenBy;
}