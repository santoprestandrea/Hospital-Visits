package com.hospital.hospital_visits.repository;

import com.hospital.hospital_visits.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/*
*
* The repository class allows me to have custom methods
* instantiated outside of the default insert ones for each associated entity
*
* */
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByNameIgnoreCase(String name);

    Page<Department> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
