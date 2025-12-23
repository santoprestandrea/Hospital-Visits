package com.hospital.hospital_visits.repository;

import com.hospital.hospital_visits.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
/*
*
* The repository class allows me to have custom methods
* instantiated outside of the default insert ones for each associated entity
*
* */
public interface DoctorRepository extends JpaRepository<Doctor, String> {

    Page<Doctor> findBySurnameContainingIgnoreCase(String surname, Pageable pageable);

    Page<Doctor> findByDepartment_Id(Long departmentId, Pageable pageable);

    Page<Doctor> findBySpecialization_Id(Long specializationId, Pageable pageable);

    Page<Doctor> findBySurnameContainingIgnoreCaseAndDepartment_Id(String surname, Long departmentId, Pageable pageable);
}
