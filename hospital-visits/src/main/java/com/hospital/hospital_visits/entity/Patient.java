package com.hospital.hospital_visits.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
/*
*
* entity fields corresponding to backend tables,
* that are tables created with the postgreSQL relational database.
* Each field must comply with the constraints imposed by the database
*
* */
@Entity
@Table(name = "patient")
public class Patient extends Person{


    @ManyToMany
    @JoinTable(
            name = "booking",
            joinColumns = @JoinColumn(name = "patient_cf"),
            inverseJoinColumns = @JoinColumn(name = "visit_code")
    )
    private Set<Visit> bookedVisits = new HashSet<>();

    public Patient() {
    }


    public Set<Visit> getBookedVisits() {
        return bookedVisits;
    }

    public void setBookedVisits(Set<Visit> bookedVisits) {
        this.bookedVisits = bookedVisits;
    }
}
