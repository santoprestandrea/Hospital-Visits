package com.hospital.hospital_visits.controller.api;

import com.hospital.hospital_visits.dto.DoctorCreateUpdateDTO;
import com.hospital.hospital_visits.dto.DoctorDTO;
import com.hospital.hospital_visits.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/*

* class that allows me to interface with the code via postman and perform some APIs,
* through the class repository and the corresponding data transfer object (dto)
*
* */
@RestController
@RequestMapping("/api/doctors")
public class DoctorRestController {

    private final DoctorService doctorService;

    public DoctorRestController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<DoctorDTO> all(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(defaultValue = "surname") String sortBy,
                               @RequestParam(defaultValue = "ASC") String direction) {
        return doctorService.getAll(page, size, sortBy, direction);
    }

    @GetMapping("/{cf}")
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorDTO byCf(@PathVariable String cf) {
        return doctorService.getByCf(cf);
    }

    @GetMapping("/search/by-surname")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<DoctorDTO> bySurname(@RequestParam String surname,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size) {
        return doctorService.searchBySurname(surname, page, size);
    }

    @GetMapping("/filter/by-department")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<DoctorDTO> byDepartment(@RequestParam Long departmentId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {
        return doctorService.filterByDepartment(departmentId, page, size);
    }

    @GetMapping("/filter/by-specialization")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<DoctorDTO> bySpecialization(@RequestParam Long specializationId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        return doctorService.filterBySpecialization(specializationId, page, size);
    }

    @PostMapping("/create-doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorDTO create(@Valid @RequestBody DoctorCreateUpdateDTO dto) {
        return doctorService.create(dto);
    }

    @PutMapping("/{cf}")
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorDTO update(@PathVariable String cf, @Valid @RequestBody DoctorCreateUpdateDTO dto) {
        return doctorService.update(cf, dto);
    }

    @DeleteMapping("/{cf}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable String cf) {
        doctorService.delete(cf);
    }
}
