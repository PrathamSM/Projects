package com.assessmentprod.userServiceV1.exception;

public class UserNameNotFoundException extends RuntimeException{

    public UserNameNotFoundException(String msg) {
        super(msg);
    }
}
