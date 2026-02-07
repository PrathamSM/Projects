package com.smeportal.profileservice.exception;

public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException(String msg) {
        super(msg);
    }
}
