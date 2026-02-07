package com.assessmentprod.evaluationserver.evaluationserver.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssessmentNotFoundException extends RuntimeException {
    public AssessmentNotFoundException(String message) {
        super(message);
    }
}

