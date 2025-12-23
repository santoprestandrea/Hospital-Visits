package com.hospital.hospital_visits.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
/*
*
* entity fields corresponding to backend tables,
* that are tables created with the postgreSQL relational database.
* Each field must comply with the constraints imposed by the database
*
* */
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    @Id
    @Column(name = "cf", length = 16)
    private String cf;

    @Column(name = "first_name", length = 60, nullable = false)
    private String firstName;

    @Column(name = "surname", length = 60, nullable = false)
    private String surname;

    @Column(name = "address", length = 120)
    private String address;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    public Person() {
    }

    public Person(String cf, String firstName, String surname, String address, LocalDate birthDate) {
        this.cf = cf;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.birthDate = birthDate;
    }

    // Getters & Setters

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
