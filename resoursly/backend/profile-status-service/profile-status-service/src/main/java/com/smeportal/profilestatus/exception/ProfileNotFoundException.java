package com.smeportal.profilestatus.exception;

public class ProfileNotFoundException extends RuntimeException{
    public ProfileNotFoundException(String msg) {
        super(msg);
    }
}