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
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private Role role;
    private String profileImageUrl;
    private LocalDateTime registrationDate;
    private String patientCf;
    private String doctorCf;


    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, Role role,
                   String profileImageUrl, LocalDateTime registrationDate,
                   String patientCf, String doctorCf) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.registrationDate = registrationDate;
        this.patientCf = patientCf;
        this.doctorCf = doctorCf;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public String getPatientCf() { return patientCf; }
    public void setPatientCf(String patientCf) { this.patientCf = patientCf; }

    public String getDoctorCf() { return doctorCf; }
    public void setDoctorCf(String doctorCf) { this.doctorCf = doctorCf; }

}
