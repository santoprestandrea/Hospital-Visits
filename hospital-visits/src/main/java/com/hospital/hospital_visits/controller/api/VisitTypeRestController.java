package com.hospital.hospital_visits.controller.api;

import com.hospital.hospital_visits.dto.VisitTypeCreateUpdateDTO;
import com.hospital.hospital_visits.dto.VisitTypeDTO;
import com.hospital.hospital_visits.service.VisitTypeService;
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
@RequestMapping("/api/visit-types")
public class VisitTypeRestController {

    private final VisitTypeService visitTypeService;

    public VisitTypeRestController(VisitTypeService visitTypeService) {
        this.visitTypeService = visitTypeService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<VisitTypeDTO> all(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  @RequestParam(defaultValue = "name") String sortBy,
                                  @RequestParam(defaultValue = "ASC") String direction) {
        return visitTypeService.getAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VisitTypeDTO byId(@PathVariable Long id) {
        return visitTypeService.getById(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<VisitTypeDTO> search(@RequestParam String name,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size) {
        return visitTypeService.searchByName(name, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public VisitTypeDTO create(@Valid @RequestBody VisitTypeCreateUpdateDTO dto) {
        return visitTypeService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VisitTypeDTO update(@PathVariable Long id, @Valid @RequestBody VisitTypeCreateUpdateDTO dto) {
        return visitTypeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        visitTypeService.delete(id);
    }
}
