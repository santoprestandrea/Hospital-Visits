package com.hospital.hospital_visits.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/*
*
* creation of the objects that we will need as a
* "transfer" (dto) for the REST CONTROLLER classes,
*  which will allow us to run the apis
*
* */
public class UpdateProfileImageForm {

    @NotBlank(message = "Profile image URL is required")
    @Size(max = 255, message = "URL too long")
    private String profileImageUrl;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
