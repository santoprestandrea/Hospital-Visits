package com.hospital.hospital_visits.dto;

import jakarta.validation.constraints.NotBlank;
/*
*
* creation of the objects that will serve as a "transfer"
*  (dto) for the REST CONTROLLER classes, which will allow us
* to execute the apis, the requests will go to modify some data
* or possibly create it
*
* */
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
