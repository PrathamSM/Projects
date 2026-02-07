//package com.assessmentprod.assessment.exception;
//
//public class ResourceNotFoundException extends Exception{
//
//    public ResourceNotFoundException(String msg) {
//        super(msg);
//    }
//}


package com.assessmentprod.assessment.exception;

public class ResourceNotFoundException extends BadRequestException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

