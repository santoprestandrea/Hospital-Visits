package com.hospital.hospital_visits.service;

import com.hospital.hospital_visits.dto.ClinicRoomCreateUpdateDTO;
import com.hospital.hospital_visits.dto.ClinicRoomDTO;
import com.hospital.hospital_visits.entity.ClinicRoom;
import com.hospital.hospital_visits.entity.Doctor;
import com.hospital.hospital_visits.exception.ResourceNotFoundException;
import com.hospital.hospital_visits.repository.ClinicRoomRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
/*
*
*  service class that allows
*  me to connect with the corresponding
*  repository of the entity and possibly
*  perform operations on the data transfer object (dto)
*
* */
@Service
public class ClinicRoomService {

    private final ClinicRoomRepository clinicRoomRepository;

    public ClinicRoomService(ClinicRoomRepository clinicRoomRepository) {
        this.clinicRoomRepository = clinicRoomRepository;
    }

    private ClinicRoomDTO toDTO(ClinicRoom r) {
        ClinicRoomDTO dto = new ClinicRoomDTO();
        dto.setId(r.getId());
        dto.setAddress(r.getAddress());
        dto.setDoctorCfs(r.getDoctors().stream().map(Doctor::getCf).collect(Collectors.toSet()));
        return dto;
    }

    public ClinicRoomDTO create(ClinicRoomCreateUpdateDTO dto) {
        ClinicRoom r = new ClinicRoom();
        r.setAddress(dto.getAddress());
        r = clinicRoomRepository.save(r);
        return toDTO(r);
    }

    public ClinicRoomDTO update(Long id, ClinicRoomCreateUpdateDTO dto) {
        ClinicRoom r = clinicRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClinicRoom not found with id " + id));
        r.setAddress(dto.getAddress());
        r = clinicRoomRepository.save(r);
        return toDTO(r);
    }

    public void delete(Long id) {
        ClinicRoom r = clinicRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClinicRoom not found with id " + id));
        clinicRoomRepository.delete(r);
    }

    public ClinicRoomDTO getById(Long id) {
        ClinicRoom r = clinicRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClinicRoom not found with id " + id));
        return toDTO(r);
    }

    public Page<ClinicRoomDTO> getAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return clinicRoomRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<ClinicRoomDTO> searchByAddress(String address, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("address").ascending());
        return clinicRoomRepository.findByAddressContainingIgnoreCase(address, pageable).map(this::toDTO);
    }
}
