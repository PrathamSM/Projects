package com.smeportal.profileservice.exception;


public class ProfileNotFoundException extends Exception{

    public ProfileNotFoundException(String errMsg) {
        super(errMsg);
    }
}
