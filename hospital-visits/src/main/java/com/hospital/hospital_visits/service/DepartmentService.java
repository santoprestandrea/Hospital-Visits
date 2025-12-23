package com.hospital.hospital_visits.service;

import com.hospital.hospital_visits.dto.DepartmentCreateUpdateDTO;
import com.hospital.hospital_visits.dto.DepartmentDTO;
import com.hospital.hospital_visits.entity.Department;
import com.hospital.hospital_visits.exception.BadRequestException;
import com.hospital.hospital_visits.exception.ResourceNotFoundException;
import com.hospital.hospital_visits.repository.DepartmentRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
/*
*
*  service class that allows
*  me to connect with the corresponding
*  repository of the entity and possibly
*  perform operations on the data transfer object (dto)
*
* */
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    private DepartmentDTO toDTO(Department d) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(d.getId());
        dto.setName(d.getName());
        dto.setDescription(d.getDescription());
        return dto;
    }

    public DepartmentDTO create(DepartmentCreateUpdateDTO dto) {
        departmentRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(x -> { throw new BadRequestException("Department name already exists"); });

        Department d = new Department(dto.getName(), dto.getDescription());
        d = departmentRepository.save(d);
        return toDTO(d);
    }

    public DepartmentDTO update(Long id, DepartmentCreateUpdateDTO dto) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));

        departmentRepository.findByNameIgnoreCase(dto.getName())
                .filter(x -> !x.getId().equals(id))
                .ifPresent(x -> { throw new BadRequestException("Department name already exists"); });

        d.setName(dto.getName());
        d.setDescription(dto.getDescription());
        d = departmentRepository.save(d);
        return toDTO(d);
    }

    public void delete(Long id) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
        departmentRepository.delete(d);
    }

    public DepartmentDTO getById(Long id) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
        return toDTO(d);
    }

    public Page<DepartmentDTO> getAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return departmentRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<DepartmentDTO> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return departmentRepository.findByNameContainingIgnoreCase(name, pageable).map(this::toDTO);
    }
}
