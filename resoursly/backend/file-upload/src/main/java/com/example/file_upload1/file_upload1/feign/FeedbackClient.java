package com.example.file_upload1.file_upload1.feign;

import com.example.file_upload1.file_upload1.dto.FeedbackRating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "feedback-service" , url ="localhost:8084/feedbacks")
public interface FeedbackClient {

    @PostMapping
    public ResponseEntity<?> createFeedback(@Valid @RequestBody FeedbackRating feedbackRating);
}
