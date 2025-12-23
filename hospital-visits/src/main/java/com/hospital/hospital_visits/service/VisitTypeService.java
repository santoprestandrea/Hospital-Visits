package com.hospital.hospital_visits.service;

import com.hospital.hospital_visits.dto.VisitTypeCreateUpdateDTO;
import com.hospital.hospital_visits.dto.VisitTypeDTO;
import com.hospital.hospital_visits.entity.VisitType;
import com.hospital.hospital_visits.exception.BadRequestException;
import com.hospital.hospital_visits.exception.ResourceNotFoundException;
import com.hospital.hospital_visits.repository.VisitTypeRepository;
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
public class VisitTypeService {

    private final VisitTypeRepository visitTypeRepository;

    public VisitTypeService(VisitTypeRepository visitTypeRepository) {
        this.visitTypeRepository = visitTypeRepository;
    }

    private VisitTypeDTO toDTO(VisitType vt) {
        VisitTypeDTO dto = new VisitTypeDTO();
        dto.setId(vt.getId());
        dto.setName(vt.getName());
        dto.setDescription(vt.getDescription());
        return dto;
    }

    public VisitTypeDTO create(VisitTypeCreateUpdateDTO dto) {
        visitTypeRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(x -> { throw new BadRequestException("VisitType name already exists"); });

        VisitType vt = new VisitType(dto.getName(), dto.getDescription());
        vt = visitTypeRepository.save(vt);
        return toDTO(vt);
    }

    public VisitTypeDTO update(Long id, VisitTypeCreateUpdateDTO dto) {
        VisitType vt = visitTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VisitType not found with id " + id));

        visitTypeRepository.findByNameIgnoreCase(dto.getName())
                .filter(x -> !x.getId().equals(id))
                .ifPresent(x -> { throw new BadRequestException("VisitType name already exists"); });

        vt.setName(dto.getName());
        vt.setDescription(dto.getDescription());
        vt = visitTypeRepository.save(vt);
        return toDTO(vt);
    }

    public void delete(Long id) {
        VisitType vt = visitTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VisitType not found with id " + id));
        visitTypeRepository.delete(vt);
    }

    public VisitTypeDTO getById(Long id) {
        VisitType vt = visitTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VisitType not found with id " + id));
        return toDTO(vt);
    }

    public Page<VisitTypeDTO> getAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return visitTypeRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<VisitTypeDTO> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return visitTypeRepository.findByNameContainingIgnoreCase(name, pageable).map(this::toDTO);
    }
}
