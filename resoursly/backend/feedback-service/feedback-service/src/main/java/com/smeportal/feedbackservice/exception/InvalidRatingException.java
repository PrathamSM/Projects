package com.smeportal.feedbackservice.exception;

public class InvalidRatingException extends RuntimeException{
    public InvalidRatingException(String msg) {
        super(msg);
    }
}
