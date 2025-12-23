package com.hospital.hospital_visits.repository;

import com.hospital.hospital_visits.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
/*
*
* The repository class allows me to have custom methods
* instantiated outside of the default insert ones for each associated entity
*
* */
public interface PatientRepository extends JpaRepository<Patient, String> {


    Page<Patient> findBySurnameContainingIgnoreCase(String surname, Pageable pageable);


    Page<Patient> findByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable);

    // Pazienti con almeno N prenotazioni (bookedVisits)
    @Query("""
           SELECT p
           FROM Patient p
           JOIN p.bookedVisits v
           GROUP BY p
           HAVING COUNT(v) >= :minVisits
           """)
    Page<Patient> findPatientsWithMinBookedVisits(long minVisits, Pageable pageable);
}
