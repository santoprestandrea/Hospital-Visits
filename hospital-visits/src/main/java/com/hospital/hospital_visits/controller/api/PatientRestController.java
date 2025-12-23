package com.hospital.hospital_visits.controller.api;

import com.hospital.hospital_visits.dto.PatientCreateUpdateDTO;
import com.hospital.hospital_visits.dto.PatientDTO;
import com.hospital.hospital_visits.entity.AppUser;
import com.hospital.hospital_visits.security.CustomUserDetails;
import com.hospital.hospital_visits.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
/*

* class that allows me to interface with the code via postman and perform some APIs,
* through the class repository and the corresponding data transfer object (dto)
*
* */
@RestController
@RequestMapping("/api/patients")
public class PatientRestController {

    private final PatientService patientService;

    public PatientRestController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<PatientDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "surname") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return patientService.getAll(page, size, sortBy, direction);
    }

    @GetMapping("/{cf}")
    @PreAuthorize("hasRole('ADMIN')")
    public PatientDTO getByCf(@PathVariable String cf) {
        return patientService.getByCf(cf);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PatientDTO create(@Valid @RequestBody PatientCreateUpdateDTO dto) {
        return patientService.create(dto);
    }

    @PutMapping("/{cf}")
    @PreAuthorize("hasRole('ADMIN')")
    public PatientDTO update(@PathVariable String cf, @Valid @RequestBody PatientCreateUpdateDTO dto) {
        return patientService.update(cf, dto);
    }

    @DeleteMapping("/{cf}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable String cf) {
        patientService.delete(cf);
    }

    //  QUERY

    @GetMapping("/search/by-surname")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<PatientDTO> searchBySurname(
            @RequestParam String surname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "surname") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return patientService.searchBySurname(surname, page, size, sortBy, direction);
    }

    @GetMapping("/search/by-birthdate")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<PatientDTO> searchByBirthDateRange(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return patientService.searchByBirthDateRange(from, to, page, size);
    }

    @GetMapping("/search/by-min-booked-visits")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<PatientDTO> searchPatientsWithMinBookedVisits(
            @RequestParam long minVisits,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return patientService.searchPatientsWithMinBookedVisits(minVisits, page, size);
    }



    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public PatientDTO me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        AppUser user = userDetails.getAppUser();
        if (user.getPatient() == null) {

            return null;
        }
        return patientService.getByCf(user.getPatient().getCf());
    }
}
