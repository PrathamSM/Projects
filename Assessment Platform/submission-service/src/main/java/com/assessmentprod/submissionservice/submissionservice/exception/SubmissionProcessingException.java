package com.assessmentprod.submissionservice.submissionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SubmissionProcessingException extends RuntimeException {
    public SubmissionProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

