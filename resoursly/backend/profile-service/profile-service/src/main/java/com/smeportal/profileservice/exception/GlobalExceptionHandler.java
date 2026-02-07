package com.smeportal.profileservice.exception;

import com.smeportal.profileservice.response.ApiResponse;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<?> handleProfileNotFound(ProfileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("error", null, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<?> handleDuplicateEmail(DuplicateEmailException ex) {
//        Map<String, String> errResponse = new HashMap<>();
//        errResponse.put("error", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(errResponse);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>("error", null, ex.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                (error) ->  errors.put(((FieldError) error).getField(),error.getDefaultMessage()));

//        Map<String, Map> errorMap = new HashMap<>();
//        errorMap.put("errors", errors);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("error", errors, "Validation Error"));
    }

    @ExceptionHandler(RestoreWithinOneMinuteException.class)
    public ResponseEntity<?> handleRestoreWithinOneMinute(RestoreWithinOneMinuteException ex) {
        Map<String, String> errResponse = new HashMap<>();
        errResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
//        Map<String, String> errResponse = new HashMap<>();
//        errResponse.put("error", "An unexpected error occurred : " + ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("error", null, "Internal Server Error"));
    }
}
