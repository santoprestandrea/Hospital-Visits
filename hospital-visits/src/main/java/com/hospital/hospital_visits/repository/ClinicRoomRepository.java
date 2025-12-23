package com.hospital.hospital_visits.repository;

import com.hospital.hospital_visits.entity.ClinicRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
/*
*
* The repository class allows me to have custom methods
* instantiated outside of the default insert ones for each associated entity
*
* */
public interface ClinicRoomRepository extends JpaRepository<ClinicRoom, Long> {

    Page<ClinicRoom> findByAddressContainingIgnoreCase(String address, Pageable pageable);
}
