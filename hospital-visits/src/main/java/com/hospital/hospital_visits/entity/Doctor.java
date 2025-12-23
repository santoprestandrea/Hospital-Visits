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
@Table(name = "doctor")
public class Doctor extends Person{

    @Column(name = "email", length = 100)
    private String email;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @ManyToMany
    @JoinTable(
            name = "works_in",
            joinColumns = @JoinColumn(name = "doctor_cf"),
            inverseJoinColumns = @JoinColumn(name = "clinic_room_id")
    )
    private Set<ClinicRoom> clinicRooms = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "performed",
            joinColumns = @JoinColumn(name = "doctor_cf"),
            inverseJoinColumns = @JoinColumn(name = "visit_id")
    )
    private Set<Visit> performedVisits = new HashSet<>();


    public Doctor() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Set<ClinicRoom> getClinicRooms() {
        return clinicRooms;
    }

    public void setClinicRooms(Set<ClinicRoom> clinicRooms) {
        this.clinicRooms = clinicRooms;
    }

    public Set<Visit> getPerformedVisits() {
        return performedVisits;
    }

    public void setPerformedVisits(Set<Visit> performedVisits) {
        this.performedVisits = performedVisits;
    }
}
