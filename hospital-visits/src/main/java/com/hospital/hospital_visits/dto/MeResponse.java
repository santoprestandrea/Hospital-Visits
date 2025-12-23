package com.hospital.hospital_visits.dto;

import com.hospital.hospital_visits.entity.Role;

import java.time.LocalDateTime;
/*
*
* creation of the objects that we will need as a
* "transfer" (dto) for the REST CONTROLLER classes,
*  which will allow us to run the apis
*
* */
public class MeResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private String profileImageUrl;
    private LocalDateTime registrationDate;
    private String patientCf;

    public MeResponse(Long id, String username, String email, Role role,
                      String profileImageUrl, LocalDateTime registrationDate,
                      String patientCf) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.registrationDate = registrationDate;
        this.patientCf = patientCf;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public String getPatientCf() { return patientCf; }
}
