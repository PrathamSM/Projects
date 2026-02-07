package com.assessmentprod.questionBankService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubjectNotFoundException extends RuntimeException{

    public SubjectNotFoundException(String msg) {
        super("We don't have following subject : " + msg);
    }
}
