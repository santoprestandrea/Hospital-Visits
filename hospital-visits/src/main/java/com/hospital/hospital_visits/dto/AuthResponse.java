package com.hospital.hospital_visits.dto;

/*
*
* creation of the objects that we will need as a
* "transfer" (dto) for the REST CONTROLLER classes,
*  which will allow us to run the apis
*
* */

public class AuthResponse {

    private String token;

    public AuthResponse() {}

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
