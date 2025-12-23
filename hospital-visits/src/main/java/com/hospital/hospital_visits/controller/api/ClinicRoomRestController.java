package com.hospital.hospital_visits.controller.api;

import com.hospital.hospital_visits.dto.ClinicRoomCreateUpdateDTO;
import com.hospital.hospital_visits.dto.ClinicRoomDTO;
import com.hospital.hospital_visits.service.ClinicRoomService;
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
@RequestMapping("/api/rooms")
public class ClinicRoomRestController {

    private final ClinicRoomService clinicRoomService;

    public ClinicRoomRestController(ClinicRoomService clinicRoomService) {
        this.clinicRoomService = clinicRoomService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<ClinicRoomDTO> all(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(defaultValue = "address") String sortBy,
                                   @RequestParam(defaultValue = "ASC") String direction) {
        return clinicRoomService.getAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClinicRoomDTO byId(@PathVariable Long id) {
        return clinicRoomService.getById(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<ClinicRoomDTO> search(@RequestParam String address,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return clinicRoomService.searchByAddress(address, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ClinicRoomDTO create(@Valid @RequestBody ClinicRoomCreateUpdateDTO dto) {
        return clinicRoomService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClinicRoomDTO update(@PathVariable Long id, @Valid @RequestBody ClinicRoomCreateUpdateDTO dto) {
        return clinicRoomService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        clinicRoomService.delete(id);
    }
}
