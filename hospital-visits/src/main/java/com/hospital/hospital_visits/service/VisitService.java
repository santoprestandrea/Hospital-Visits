package com.hospital.hospital_visits.service;

import com.hospital.hospital_visits.dto.VisitCreateUpdateDTO;
import com.hospital.hospital_visits.dto.VisitDTO;
import com.hospital.hospital_visits.entity.*;
import com.hospital.hospital_visits.exception.BadRequestException;
import com.hospital.hospital_visits.exception.ResourceNotFoundException;
import com.hospital.hospital_visits.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
public class VisitService {

    private final VisitRepository visitRepository;
    private final DepartmentRepository departmentRepository;
    private final VisitTypeRepository visitTypeRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public VisitService(VisitRepository visitRepository,
                        DepartmentRepository departmentRepository,
                        VisitTypeRepository visitTypeRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository) {
        this.visitRepository = visitRepository;
        this.departmentRepository = departmentRepository;
        this.visitTypeRepository = visitTypeRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    private VisitDTO toDTO(Visit v) {
        VisitDTO dto = new VisitDTO();
        dto.setId(v.getId());
        dto.setDate(v.getDate());

        if (v.getDepartment() != null) {
            dto.setDepartmentId(v.getDepartment().getId());
            dto.setDepartmentName(v.getDepartment().getName());
        }
        if (v.getVisitType() != null) {
            dto.setVisitTypeId(v.getVisitType().getId());
            dto.setVisitTypeName(v.getVisitType().getName());
        }

        dto.setDoctorCfs(v.getDoctors().stream().map(Doctor::getCf).collect(Collectors.toSet()));
        dto.setPatientCfs(v.getPatients().stream().map(Patient::getCf).collect(Collectors.toSet()));
        return dto;
    }

    public VisitDTO create(VisitCreateUpdateDTO dto) {
        if (dto.getDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot create a visit in the past");
        }

        Department dept = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + dto.getDepartmentId()));

        VisitType vt = visitTypeRepository.findById(dto.getVisitTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("VisitType not found with id " + dto.getVisitTypeId()));

        Visit v = new Visit();
        v.setDate(dto.getDate());
        v.setDepartment(dept);
        v.setVisitType(vt);

        if (dto.getDoctorCfs() != null && !dto.getDoctorCfs().isEmpty()) {
            Set<Doctor> doctors = dto.getDoctorCfs().stream()
                    .map(cf -> doctorRepository.findById(cf)
                            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with CF " + cf)))
                    .collect(Collectors.toSet());
            v.setDoctors(doctors);
        }

        v = visitRepository.save(v);
        return toDTO(v);
    }

    public VisitDTO update(Long id, VisitCreateUpdateDTO dto) {
        Visit v = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id " + id));

        if (dto.getDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot set a visit date in the past");
        }

        Department dept = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + dto.getDepartmentId()));

        VisitType vt = visitTypeRepository.findById(dto.getVisitTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("VisitType not found with id " + dto.getVisitTypeId()));

        v.setDate(dto.getDate());
        v.setDepartment(dept);
        v.setVisitType(vt);

        // sostituisce l'assegnazione medici
        if (dto.getDoctorCfs() != null) {
            Set<Doctor> doctors = dto.getDoctorCfs().stream()
                    .map(cf -> doctorRepository.findById(cf)
                            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with CF " + cf)))
                    .collect(Collectors.toSet());
            v.setDoctors(doctors);
        }

        v = visitRepository.save(v);
        return toDTO(v);
    }

    public void delete(Long id) {
        Visit v = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id " + id));
        visitRepository.delete(v);
    }

    public VisitDTO getById(Long id) {
        return toDTO(visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id " + id)));
    }

    public Page<VisitDTO> getAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return visitRepository.findAll(pageable).map(this::toDTO);
    }

    // ---- Booking / Cancel booking ----

    public VisitDTO bookVisit(Long visitId, String patientCf) {
        Visit v = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id " + visitId));

        if (v.getDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot book a visit in the past");
        }

        Patient p = patientRepository.findById(patientCf)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with CF " + patientCf));

        // evita doppia prenotazione
        if (v.getPatients().stream().anyMatch(x -> x.getCf().equals(patientCf))) {
            throw new BadRequestException("Patient already booked this visit");
        }

        // manteniamo consistenza su entrambi i lati della relazione ManyToMany
        v.getPatients().add(p);
        p.getBookedVisits().add(v);

        visitRepository.save(v);
        patientRepository.save(p);

        return toDTO(v);
    }

    public VisitDTO cancelBooking(Long visitId, String patientCf) {
        Visit v = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found with id " + visitId));

        Patient p = patientRepository.findById(patientCf)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with CF " + patientCf));

        boolean removedFromVisit = v.getPatients().removeIf(x -> x.getCf().equals(patientCf));
        boolean removedFromPatient = p.getBookedVisits().removeIf(x -> x.getId().equals(visitId));

        if (!removedFromVisit || !removedFromPatient) {
            throw new BadRequestException("Booking not found for this patient/visit");
        }

        visitRepository.save(v);
        patientRepository.save(p);

        return toDTO(v);
    }

    // ---- Query avanzate ----

    public Page<VisitDTO> search(Long departmentId, Long visitTypeId, LocalDate from, LocalDate to,
                                 int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return visitRepository.search(departmentId, visitTypeId, from, to, pageable).map(this::toDTO);
    }

    public Page<VisitDTO> byDoctor(String doctorCf, int page, int size) {
        return visitRepository.findByDoctorCf(doctorCf, PageRequest.of(page, size)).map(this::toDTO);
    }

    public Page<VisitDTO> bookedByPatient(String patientCf, int page, int size) {
        return visitRepository.findBookedByPatientCf(patientCf, PageRequest.of(page, size)).map(this::toDTO);
    }

    public java.util.List<String> bookingsByVisitType() {
        return visitRepository.countBookingsByVisitType().stream()
                .map(row -> row[0] + ": " + row[1])
                .toList();
    }
}
