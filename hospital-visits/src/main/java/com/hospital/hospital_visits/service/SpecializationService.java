package com.hospital.hospital_visits.service;

import com.hospital.hospital_visits.dto.SpecializationCreateUpdateDTO;
import com.hospital.hospital_visits.dto.SpecializationDTO;
import com.hospital.hospital_visits.entity.Specialization;
import com.hospital.hospital_visits.exception.BadRequestException;
import com.hospital.hospital_visits.exception.ResourceNotFoundException;
import com.hospital.hospital_visits.repository.SpecializationRepository;
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
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    private SpecializationDTO toDTO(Specialization s) {
        SpecializationDTO dto = new SpecializationDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setDescription(s.getDescription());
        return dto;
    }

    public SpecializationDTO create(SpecializationCreateUpdateDTO dto) {
        specializationRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(x -> { throw new BadRequestException("Specialization name already exists"); });

        Specialization s = new Specialization(dto.getName(), dto.getDescription());
        s = specializationRepository.save(s);
        return toDTO(s);
    }

    public SpecializationDTO update(Long id, SpecializationCreateUpdateDTO dto) {
        Specialization s = specializationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id " + id));

        specializationRepository.findByNameIgnoreCase(dto.getName())
                .filter(x -> !x.getId().equals(id))
                .ifPresent(x -> { throw new BadRequestException("Specialization name already exists"); });

        s.setName(dto.getName());
        s.setDescription(dto.getDescription());
        s = specializationRepository.save(s);
        return toDTO(s);
    }

    public void delete(Long id) {
        Specialization s = specializationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id " + id));
        specializationRepository.delete(s);
    }

    public SpecializationDTO getById(Long id) {
        Specialization s = specializationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id " + id));
        return toDTO(s);
    }

    public Page<SpecializationDTO> getAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return specializationRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<SpecializationDTO> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return specializationRepository.findByNameContainingIgnoreCase(name, pageable).map(this::toDTO);
    }
}
