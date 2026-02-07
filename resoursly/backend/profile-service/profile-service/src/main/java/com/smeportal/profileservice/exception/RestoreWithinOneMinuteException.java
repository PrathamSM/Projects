package com.smeportal.profileservice.exception;

public class RestoreWithinOneMinuteException extends RuntimeException {
    public RestoreWithinOneMinuteException(String message) {
        super(message);
    }
}

