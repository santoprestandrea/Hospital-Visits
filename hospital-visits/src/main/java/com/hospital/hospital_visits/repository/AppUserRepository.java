package com.hospital.hospital_visits.repository;

import com.hospital.hospital_visits.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/*
*
* The repository class allows me to have custom methods
* instantiated outside of the default insert ones for each associated entity
*
* */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);


    boolean existsByDoctor_Cf(String doctorCf);


    Optional<AppUser> findByDoctor_Cf(String doctorCf);
}
