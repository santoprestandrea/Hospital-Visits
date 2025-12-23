package com.hospital.hospital_visits.controller.api;

import com.hospital.hospital_visits.dto.SpecializationCreateUpdateDTO;
import com.hospital.hospital_visits.dto.SpecializationDTO;
import com.hospital.hospital_visits.service.SpecializationService;
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
@RequestMapping("/api/specializations")
public class SpecializationRestController {

    private final SpecializationService specializationService;

    public SpecializationRestController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<SpecializationDTO> all(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size,
                                       @RequestParam(defaultValue = "name") String sortBy,
                                       @RequestParam(defaultValue = "ASC") String direction) {
        return specializationService.getAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public SpecializationDTO byId(@PathVariable Long id) {
        return specializationService.getById(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<SpecializationDTO> search(@RequestParam String name,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        return specializationService.searchByName(name, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public SpecializationDTO create(@Valid @RequestBody SpecializationCreateUpdateDTO dto) {
        return specializationService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public SpecializationDTO update(@PathVariable Long id, @Valid @RequestBody SpecializationCreateUpdateDTO dto) {
        return specializationService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        specializationService.delete(id);
    }
}
