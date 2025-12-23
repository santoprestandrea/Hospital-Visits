package com.hospital.hospital_visits.controller.api;

import com.hospital.hospital_visits.dto.VisitCreateUpdateDTO;
import com.hospital.hospital_visits.dto.VisitDTO;
import com.hospital.hospital_visits.entity.AppUser;
import com.hospital.hospital_visits.security.CustomUserDetails;
import com.hospital.hospital_visits.service.VisitService;
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
@RequestMapping("/api/visits")
public class VisitRestController {

    private final VisitService visitService;

    public VisitRestController(VisitService visitService) {
        this.visitService = visitService;
    }



    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public Page<VisitDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return visitService.getAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    public VisitDTO getById(@PathVariable Long id) {
        return visitService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public VisitDTO create(@Valid @RequestBody VisitCreateUpdateDTO dto) {
        return visitService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public VisitDTO update(@PathVariable Long id, @Valid @RequestBody VisitCreateUpdateDTO dto) {
        return visitService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        visitService.delete(id);
    }



    @PostMapping("/{id}/book")
    @PreAuthorize("hasRole('PATIENT')")
    public VisitDTO book(@PathVariable Long id,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        AppUser u = userDetails.getAppUser();
        String patientCf = u.getPatient().getCf(); // Patient PK = cf
        return visitService.bookVisit(id, patientCf);
    }

    @DeleteMapping("/{id}/book")
    @PreAuthorize("hasRole('PATIENT')")
    public VisitDTO cancel(@PathVariable Long id,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        String patientCf = userDetails.getAppUser().getPatient().getCf();
        return visitService.cancelBooking(id, patientCf);
    }


    @GetMapping("/me/bookings")
    @PreAuthorize("hasRole('PATIENT')")
    public Page<VisitDTO> myBookings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size) {
        String patientCf = userDetails.getAppUser().getPatient().getCf();
        return visitService.bookedByPatient(patientCf, page, size);
    }

    @GetMapping("/me/assigned")
    @PreAuthorize("hasRole('DOCTOR')")
    public Page<VisitDTO> myAssignedVisits(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {

        if (userDetails.getAppUser().getDoctor() == null) {
            throw new com.hospital.hospital_visits.exception.BadRequestException("Logged user is not linked to a Doctor");
        }

        String doctorCf = userDetails.getAppUser().getDoctor().getCf();
        return visitService.byDoctor(doctorCf, page, size);
    }


    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public Page<VisitDTO> search(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long visitTypeId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return visitService.search(departmentId, visitTypeId, from, to, page, size, sortBy, direction);
    }


    @GetMapping("/stats/bookings-by-visit-type")
    @PreAuthorize("hasRole('ADMIN')")
    public java.util.List<String> bookingsByVisitType() {
        return visitService.bookingsByVisitType();
    }
}
