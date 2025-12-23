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
public class UpdateProfileImageRequest {

    @NotBlank(message = "profileImageUrl is required")
    private String profileImageUrl;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
