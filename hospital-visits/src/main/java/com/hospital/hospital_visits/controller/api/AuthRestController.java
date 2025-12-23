package com.hospital.hospital_visits.controller.api;

import com.hospital.hospital_visits.dto.AuthResponse;
import com.hospital.hospital_visits.dto.LoginRequest;
import com.hospital.hospital_visits.dto.RegisterDoctorUserRequest;
import com.hospital.hospital_visits.dto.RegisterRequest;
import com.hospital.hospital_visits.entity.AppUser;
import com.hospital.hospital_visits.entity.Doctor;
import com.hospital.hospital_visits.entity.Patient;
import com.hospital.hospital_visits.entity.Role;
import com.hospital.hospital_visits.exception.ResourceNotFoundException;
import com.hospital.hospital_visits.repository.AppUserRepository;
import com.hospital.hospital_visits.repository.DoctorRepository;
import com.hospital.hospital_visits.repository.PatientRepository;
import com.hospital.hospital_visits.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/*

* This class allows us to log in by comparing the username and password.
In addition, we can register through the various fields of the corresponding
* data transfer object (dto) of the various users.
After that, the same class allows us to map an address and therefore subsequently
*  run an API via postman to check that everything is successful.
*
* */

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AppUserRepository appUserRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthRestController(AppUserRepository appUserRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              PasswordEncoder passwordEncoder,
                              AuthenticationManager authenticationManager,
                              JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

        if (req.getRole() == null) {
            return ResponseEntity.badRequest().body("Role is required: PATIENT");
        }

        if (req.getRole() == Role.ADMIN) {
            throw new AccessDeniedException("ADMIN role cannot be self-registered");
        }

        if (req.getRole() == Role.DOCTOR) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Doctor accounts must be created by an ADMIN via /api/auth/register-doctor");
        }

        if (req.getRole() != Role.PATIENT) {
            return ResponseEntity.badRequest().body("Invalid role");
        }

        if (appUserRepository.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        }
        if (appUserRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
        if (patientRepository.findById(req.getCf()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A patient with this fiscal code already exists");
        }

        Patient patient = new Patient();
        patient.setCf(req.getCf());
        patient.setFirstName(req.getFirstName());
        patient.setSurname(req.getSurname());
        patient.setAddress(req.getAddress());
        patient.setBirthDate(req.getBirthDate());
        patientRepository.save(patient);

        AppUser user = new AppUser();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.PATIENT);
        user.setPatient(patient);
        user.setProfileImageUrl("https://example.com/default-avatar.png");
        user.setRegistrationDate(LocalDateTime.now());

        appUserRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/register-doctor")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerDoctorUser(@Valid @RequestBody RegisterDoctorUserRequest req) {

        if (appUserRepository.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        }
        if (appUserRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        Doctor doctor = doctorRepository.findById(req.getDoctorCf())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with CF " + req.getDoctorCf()));

        boolean alreadyLinked = appUserRepository.findAll().stream()
                .anyMatch(u -> u.getDoctor() != null && req.getDoctorCf().equals(u.getDoctor().getCf()));
        if (alreadyLinked) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("This doctor is already linked to an existing user account");
        }

        AppUser user = new AppUser();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.DOCTOR);
        user.setDoctor(doctor);
        user.setPatient(null);

        if (req.getProfileImageUrl() != null && !req.getProfileImageUrl().isBlank()) {
            user.setProfileImageUrl(req.getProfileImageUrl());
        } else {
            user.setProfileImageUrl("https://example.com/default-doctor-avatar.png");
        }
        user.setRegistrationDate(LocalDateTime.now());

        appUserRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
