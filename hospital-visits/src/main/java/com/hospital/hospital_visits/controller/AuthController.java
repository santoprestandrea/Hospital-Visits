package com.hospital.hospital_visits.controller;

import com.hospital.hospital_visits.dto.RegisterRequest;
import com.hospital.hospital_visits.dto.RegistrationForm;
import com.hospital.hospital_visits.entity.AppUser;
import com.hospital.hospital_visits.entity.Patient;
import com.hospital.hospital_visits.entity.Role;
import com.hospital.hospital_visits.repository.AppUserRepository;
import com.hospital.hospital_visits.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

/*
*
* This controller allows us to control the behavior of web pages
* developed using HTML templates and manage all the operations that
* can be performed here
*
* */

@Controller
public class AuthController {

    private final AppUserRepository appUserRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository appUserRepository,
                          PatientRepository patientRepository,
                          PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }


    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("registrationForm") RegistrationForm form,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "register";
        }


// Controllo username già usato
        if (appUserRepository.findByUsername(form.getUsername()).isPresent()) {
            model.addAttribute("usernameError", "Username is already taken.");
            return "register";
        }

// Controllo email già usata
        if (appUserRepository.findByEmail(form.getEmail()).isPresent()) {
            model.addAttribute("emailError", "Email is already in use.");
            return "register";
        }



        if (patientRepository.findById(form.getCf()).isPresent()) {
            model.addAttribute("cfError", "A patient with this fiscal code already exists.");
            return "register";
        }


        Patient patient = new Patient();
        patient.setCf(form.getCf());
        patient.setFirstName(form.getFirstName());
        patient.setSurname(form.getSurname());
        patient.setAddress(form.getAddress());
        patient.setBirthDate(form.getBirthDate());

        patientRepository.save(patient);
        AppUser appUser = new AppUser();
        appUser.setUsername(form.getUsername());
        appUser.setEmail(form.getEmail());
        appUser.setPassword(passwordEncoder.encode(form.getPassword()));
        appUser.setRole(Role.PATIENT);
        appUser.setPatient(patient);


        appUser.setProfileImageUrl("https://example.com/default-avatar.png");

        appUser.setRegistrationDate(LocalDateTime.now());

        appUserRepository.save(appUser);

        return "redirect:/login";
    }
}
