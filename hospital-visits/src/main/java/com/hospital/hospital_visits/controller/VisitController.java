package com.hospital.hospital_visits.controller;

import com.hospital.hospital_visits.entity.AppUser;
import com.hospital.hospital_visits.entity.Patient;
import com.hospital.hospital_visits.entity.Visit;
import com.hospital.hospital_visits.repository.PatientRepository;
import com.hospital.hospital_visits.repository.VisitRepository;
import com.hospital.hospital_visits.security.CustomUserDetails;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
/*
*
* This controller allows us to control the behavior of web pages
* developed using HTML templates and manage all the operations that
* can be performed here
*
* */
@Controller
@RequestMapping("/visits")
public class VisitController {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;

    public VisitController(VisitRepository visitRepository,
                           PatientRepository patientRepository) {
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping
    public String listVisits(Model model,
                             @RequestParam(value = "message", required = false) String message,
                             @RequestParam(value = "error", required = false) String error) {
        List<Visit> visits = visitRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
        model.addAttribute("visits", visits);

        if (message != null) {
            model.addAttribute("message", message);
        }
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }

        return "visits";
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public String myVisits(Authentication authentication, Model model) {

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            model.addAttribute("errorMessage", "Invalid user details.");
            return "my-visits";
        }

        AppUser appUser = customUserDetails.getAppUser();
        Patient linkedPatient = appUser.getPatient();

        if (linkedPatient == null) {
            model.addAttribute("errorMessage", "Current user is not linked to a patient.");
            return "my-visits";
        }


        Patient patient = patientRepository.findById(linkedPatient.getCf())
                .orElse(null);

        if (patient == null) {
            model.addAttribute("errorMessage", "Patient not found.");
            return "my-visits";
        }

        List<Visit> visits = patient.getBookedVisits()
                .stream()
                .sorted((v1, v2) -> v1.getDate().compareTo(v2.getDate()))
                .toList();

        model.addAttribute("patient", patient);
        model.addAttribute("visits", visits);

        return "my-visits";
    }

    @PostMapping("/{visitCode}/book")
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public String bookVisit(@PathVariable("visitCode") Long visitCode,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {


        Optional<Visit> optionalVisit = visitRepository.findById(visitCode);
        if (optionalVisit.isEmpty()) {
            redirectAttributes.addAttribute("error", "Visit not found.");
            return "redirect:/visits";
        }
        Visit visit = optionalVisit.get();


        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            redirectAttributes.addAttribute("error", "Invalid user details.");
            return "redirect:/visits";
        }

        AppUser appUser = customUserDetails.getAppUser();
        Patient linkedPatient = appUser.getPatient();

        if (linkedPatient == null) {
            redirectAttributes.addAttribute("error", "Current user is not linked to a patient.");
            return "redirect:/visits";
        }


        Patient patient = patientRepository.findById(linkedPatient.getCf())
                .orElse(null);

        if (patient == null) {
            redirectAttributes.addAttribute("error", "Patient not found.");
            return "redirect:/visits";
        }


        if (!patient.getBookedVisits().contains(visit)) {
            patient.getBookedVisits().add(visit);
            visit.getPatients().add(patient);

            patientRepository.save(patient);
            visitRepository.save(visit);

            redirectAttributes.addAttribute("message", "Visit successfully booked.");
        } else {
            redirectAttributes.addAttribute("message", "You have already booked this visit.");
        }

        return "redirect:/visits";
    }
}
