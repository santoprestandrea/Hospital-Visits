package com.hospital.hospital_visits.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
/*
*
* entity fields corresponding to backend tables,
* that are tables created with the postgreSQL relational database.
* Each field must comply with the constraints imposed by the database
*
* */
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;


    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "role", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @ManyToOne
    @JoinColumn(name = "patient_cf")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_cf", unique = true)
    private Doctor doctor;


    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


    public AppUser() {
    }

    public AppUser(
            String username,
            String email,
            String password,
            Role role,
            String profileImageUrl,
            LocalDateTime registrationDate,
            Patient patient
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.registrationDate = registrationDate;
        this.patient = patient;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
