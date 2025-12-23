package com.hospital.hospital_visits.controller.api;

import com.hospital.hospital_visits.dto.DepartmentCreateUpdateDTO;
import com.hospital.hospital_visits.dto.DepartmentDTO;
import com.hospital.hospital_visits.service.DepartmentService;
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
@RequestMapping("/api/departments")
public class DepartmentRestController {

    private final DepartmentService departmentService;

    public DepartmentRestController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<DepartmentDTO> all(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(defaultValue = "name") String sortBy,
                                   @RequestParam(defaultValue = "ASC") String direction) {
        return departmentService.getAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DepartmentDTO byId(@PathVariable Long id) {
        return departmentService.getById(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<DepartmentDTO> search(@RequestParam String name,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return departmentService.searchByName(name, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DepartmentDTO create(@Valid @RequestBody DepartmentCreateUpdateDTO dto) {
        return departmentService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DepartmentDTO update(@PathVariable Long id, @Valid @RequestBody DepartmentCreateUpdateDTO dto) {
        return departmentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        departmentService.delete(id);
    }
}
