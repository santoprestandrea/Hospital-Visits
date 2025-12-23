package com.hospital.hospital_visits.exception;
/*
*
* custom exception class that allows us to insert a custom error message
*
* */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
