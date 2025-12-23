package com.hospital.hospital_visits.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/*
*
* creation of the objects that will serve as a "transfer"
*  (dto) for the REST CONTROLLER classes, which will allow us
* to execute the apis, the requests will go to modify some data
* or possibly create it
*
* */
public class RegisterDoctorUserRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "Doctor CF is required")
    private String doctorCf;

    private String profileImageUrl;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDoctorCf() { return doctorCf; }
    public void setDoctorCf(String doctorCf) { this.doctorCf = doctorCf; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}
