package com.hospital.hospital_visits.entity;

import jakarta.persistence.*;

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
@Table(name = "clinic_room")
public class ClinicRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "address", length = 120, nullable = false)
    private String address;

    @ManyToMany(mappedBy = "clinicRooms")
    private Set<Doctor> doctors = new HashSet<>();

    public ClinicRoom() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }
}
