package com.hospital.hospital_visits.exception;
/*
*
* custom exception class that allows us to insert a custom error message
*
* */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
