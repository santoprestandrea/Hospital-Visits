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
@Table(name = "visit_type")
public class VisitType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(mappedBy = "visitType")
    private Set<Visit> visits = new HashSet<>();

    public VisitType() {
    }

    public VisitType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Visit> getVisits() {
        return visits;
    }
}
