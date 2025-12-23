package com.hospital.hospital_visits.service;

import com.hospital.hospital_visits.dto.PatientCreateUpdateDTO;
import com.hospital.hospital_visits.dto.PatientDTO;
import com.hospital.hospital_visits.entity.Patient;
import com.hospital.hospital_visits.exception.BadRequestException;
import com.hospital.hospital_visits.exception.ResourceNotFoundException;
import com.hospital.hospital_visits.repository.PatientRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
/*
*
*  service class that allows
*  me to connect with the corresponding
*  repository of the entity and possibly
*  perform operations on the data transfer object (dto)
*
* */
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    private PatientDTO toDTO(Patient p) {
        PatientDTO dto = new PatientDTO();
        dto.setCf(p.getCf());
        dto.setFirstName(p.getFirstName());
        dto.setSurname(p.getSurname());
        dto.setAddress(p.getAddress());
        dto.setBirthDate(p.getBirthDate());
        return dto;
    }

    private void apply(PatientCreateUpdateDTO dto, Patient p, boolean allowCfChange) {
        if (allowCfChange) {
            p.setCf(dto.getCf());
        }
        p.setFirstName(dto.getFirstName());
        p.setSurname(dto.getSurname());
        p.setAddress(dto.getAddress());
        p.setBirthDate(dto.getBirthDate());
    }

    public PatientDTO create(PatientCreateUpdateDTO dto) {
        if (patientRepository.existsById(dto.getCf())) {
            throw new BadRequestException("Patient with CF " + dto.getCf() + " already exists");
        }

        Patient p = new Patient();
        apply(dto, p, true);
        p = patientRepository.save(p);
        return toDTO(p);
    }

    public PatientDTO update(String cf, PatientCreateUpdateDTO dto) {
        Patient p = patientRepository.findById(cf)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with CF " + cf));


        if (!cf.equals(dto.getCf())) {
            throw new BadRequestException("CF cannot be changed (it is the primary key)");
        }

        apply(dto, p, false);
        p = patientRepository.save(p);
        return toDTO(p);
    }

    public void delete(String cf) {
        Patient p = patientRepository.findById(cf)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with CF " + cf));
        patientRepository.delete(p);
    }

    public PatientDTO getByCf(String cf) {
        Patient p = patientRepository.findById(cf)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with CF " + cf));
        return toDTO(p);
    }

    public Page<PatientDTO> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return patientRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<PatientDTO> searchBySurname(String surname, int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return patientRepository.findBySurnameContainingIgnoreCase(surname, pageable).map(this::toDTO);
    }

    public Page<PatientDTO> searchByBirthDateRange(LocalDate from, LocalDate to, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("birthDate").ascending());
        return patientRepository.findByBirthDateBetween(from, to, pageable).map(this::toDTO);
    }

    public Page<PatientDTO> searchPatientsWithMinBookedVisits(long minVisits, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return patientRepository.findPatientsWithMinBookedVisits(minVisits, pageable).map(this::toDTO);
    }
}
