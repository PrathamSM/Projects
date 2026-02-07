package com.smeportal.feedbackservice.exception;

import com.smeportal.feedbackservice.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<?> handleInvalidRating(InvalidRatingException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("error", null, ex.getMessage()));

    }

    @ExceptionHandler(FeedbackNotFoundException.class)
    public ResponseEntity<?> handleProfileNotFound(FeedbackNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("error", null, ex.getMessage()));
    }

}
