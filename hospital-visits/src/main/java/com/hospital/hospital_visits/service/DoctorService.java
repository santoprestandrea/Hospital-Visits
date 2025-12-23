package com.hospital.hospital_visits.service;

import com.hospital.hospital_visits.dto.DoctorCreateUpdateDTO;
import com.hospital.hospital_visits.dto.DoctorDTO;
import com.hospital.hospital_visits.entity.*;
import com.hospital.hospital_visits.exception.BadRequestException;
import com.hospital.hospital_visits.exception.ResourceNotFoundException;
import com.hospital.hospital_visits.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Set;
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
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final SpecializationRepository specializationRepository;
    private final ClinicRoomRepository clinicRoomRepository;

    public DoctorService(DoctorRepository doctorRepository,
                         DepartmentRepository departmentRepository,
                         SpecializationRepository specializationRepository,
                         ClinicRoomRepository clinicRoomRepository) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.specializationRepository = specializationRepository;
        this.clinicRoomRepository = clinicRoomRepository;
    }

    private DoctorDTO toDTO(Doctor d) {
        DoctorDTO dto = new DoctorDTO();
        dto.setCf(d.getCf());
        dto.setFirstName(d.getFirstName());
        dto.setSurname(d.getSurname());
        dto.setAddress(d.getAddress());
        dto.setBirthDate(d.getBirthDate());
        dto.setEmail(d.getEmail());

        if (d.getDepartment() != null) {
            dto.setDepartmentId(d.getDepartment().getId());
            dto.setDepartmentName(d.getDepartment().getName());
        }
        if (d.getSpecialization() != null) {
            dto.setSpecializationId(d.getSpecialization().getId());
            dto.setSpecializationName(d.getSpecialization().getName());
        }

        dto.setClinicRoomIds(d.getClinicRooms().stream().map(ClinicRoom::getId).collect(Collectors.toSet()));
        dto.setPerformedVisitIds(d.getPerformedVisits().stream().map(Visit::getId).collect(Collectors.toSet()));
        return dto;
    }

    public DoctorDTO create(DoctorCreateUpdateDTO dto) {
        if (doctorRepository.existsById(dto.getCf())) {
            throw new BadRequestException("Doctor with CF already exists");
        }

        Department dept = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + dto.getDepartmentId()));

        Specialization spec = specializationRepository.findById(dto.getSpecializationId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id " + dto.getSpecializationId()));

        Doctor d = new Doctor();
        d.setCf(dto.getCf());
        d.setFirstName(dto.getFirstName());
        d.setSurname(dto.getSurname());
        d.setAddress(dto.getAddress());
        d.setBirthDate(dto.getBirthDate());
        d.setEmail(dto.getEmail());
        d.setDepartment(dept);
        d.setSpecialization(spec);

        if (dto.getClinicRoomIds() != null) {
            Set<ClinicRoom> rooms = dto.getClinicRoomIds().stream()
                    .map(id -> clinicRoomRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("ClinicRoom not found with id " + id)))
                    .collect(Collectors.toSet());
            d.setClinicRooms(rooms);
        }

        d = doctorRepository.save(d);
        return toDTO(d);
    }

    public DoctorDTO update(String cf, DoctorCreateUpdateDTO dto) {
        Doctor d = doctorRepository.findById(cf)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with CF " + cf));

        if (!cf.equals(dto.getCf())) {
            throw new BadRequestException("CF cannot be changed (it is the primary key)");
        }

        Department dept = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + dto.getDepartmentId()));

        Specialization spec = specializationRepository.findById(dto.getSpecializationId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id " + dto.getSpecializationId()));

        d.setFirstName(dto.getFirstName());
        d.setSurname(dto.getSurname());
        d.setAddress(dto.getAddress());
        d.setBirthDate(dto.getBirthDate());
        d.setEmail(dto.getEmail());
        d.setDepartment(dept);
        d.setSpecialization(spec);

        if (dto.getClinicRoomIds() != null) {
            Set<ClinicRoom> rooms = dto.getClinicRoomIds().stream()
                    .map(id -> clinicRoomRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("ClinicRoom not found with id " + id)))
                    .collect(Collectors.toSet());
            d.setClinicRooms(rooms);
        }

        d = doctorRepository.save(d);
        return toDTO(d);
    }

    public void delete(String cf) {
        Doctor d = doctorRepository.findById(cf)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with CF " + cf));
        doctorRepository.delete(d);
    }

    public DoctorDTO getByCf(String cf) {
        Doctor d = doctorRepository.findById(cf)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with CF " + cf));
        return toDTO(d);
    }

    public Page<DoctorDTO> getAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return doctorRepository.findAll(pageable).map(this::toDTO);
    }

    // query “real use cases”
    public Page<DoctorDTO> searchBySurname(String surname, int page, int size) {
        return doctorRepository.findBySurnameContainingIgnoreCase(surname, PageRequest.of(page, size)).map(this::toDTO);
    }

    public Page<DoctorDTO> filterByDepartment(Long departmentId, int page, int size) {
        return doctorRepository.findByDepartment_Id(departmentId, PageRequest.of(page, size)).map(this::toDTO);
    }

    public Page<DoctorDTO> filterBySpecialization(Long specializationId, int page, int size) {
        return doctorRepository.findBySpecialization_Id(specializationId, PageRequest.of(page, size)).map(this::toDTO);
    }
}
