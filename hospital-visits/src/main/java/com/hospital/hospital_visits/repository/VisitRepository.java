package com.hospital.hospital_visits.repository;

import com.hospital.hospital_visits.entity.Visit;
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
public interface VisitRepository extends JpaRepository<Visit, Long> {

    Page<Visit> findByDateBetween(LocalDate from, LocalDate to, Pageable pageable);

    Page<Visit> findByDepartment_Id(Long departmentId, Pageable pageable);

    Page<Visit> findByVisitType_Id(Long visitTypeId, Pageable pageable);

 // Visits assigned to a doctor
    @Query("""
           SELECT v
           FROM Visit v
           JOIN v.doctors d
           WHERE d.cf = :doctorCf
           """)
    Page<Visit> findByDoctorCf(String doctorCf, Pageable pageable);

 // Visits booked by a patient
    @Query("""
           SELECT v
           FROM Visit v
           JOIN v.patients p
           WHERE p.cf = :patientCf
           """)
    Page<Visit> findBookedByPatientCf(String patientCf, Pageable pageable);

    // Query: department + visit type + date range
    @Query("""
           SELECT v
           FROM Visit v
           WHERE (:departmentId IS NULL OR v.department.id = :departmentId)
             AND (:visitTypeId  IS NULL OR v.visitType.id  = :visitTypeId)
             AND (:from IS NULL OR v.date >= :from)
             AND (:to   IS NULL OR v.date <= :to)
           """)
    Page<Visit> search(Long departmentId, Long visitTypeId, LocalDate from, LocalDate to, Pageable pageable);

    // Aggregation: number of reservations per visit type
    @Query("""
           SELECT vt.name, COUNT(p)
           FROM Visit v
           JOIN v.visitType vt
           LEFT JOIN v.patients p
           GROUP BY vt.name
           """)
    java.util.List<Object[]> countBookingsByVisitType();
}
