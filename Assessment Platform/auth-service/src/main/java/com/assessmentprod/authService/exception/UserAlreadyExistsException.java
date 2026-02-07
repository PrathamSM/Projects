package com.assessmentprod.authService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String data, String property) {
        super("User already exists with " + data + " : " + property);
    }
}
