package com.hospital.hospital_visits.dto;

import com.hospital.hospital_visits.entity.Role;

import java.time.LocalDate;
/*
*
* creation of the objects that will serve as a "transfer"
*  (dto) for the REST CONTROLLER classes, which will allow us
* to execute the apis, the requests will go to modify some data
* or possibly create it
*
* */
public class RegisterRequest {

    private String username;
    private String email;
    private String password;

    private String cf;
    private String firstName;
    private String surname;
    private String address;
    private LocalDate birthDate;

    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getCf() {
        return cf;
    }

    public String getAddress() {
        return address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}