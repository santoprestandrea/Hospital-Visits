package com.hospital.hospital_visits.controller;
/*
*
* This controller allows us to control the behavior of web pages
* developed using HTML templates and manage all the operations that
* can be performed here
*
* */
import com.hospital.hospital_visits.entity.*;
import com.hospital.hospital_visits.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppUserRepository appUserRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ClinicRoomRepository clinicRoomRepository;
    private final VisitRepository visitRepository;
    private final PasswordEncoder passwordEncoder;
    private final VisitTypeRepository visitTypeRepository;


    public AdminController(AppUserRepository appUserRepository,
                           PatientRepository patientRepository,
                           DoctorRepository doctorRepository,
                           ClinicRoomRepository clinicRoomRepository,
                           VisitRepository visitRepository,
                           PasswordEncoder passwordEncoder,
                           VisitTypeRepository visitTypeRepository) {
        this.appUserRepository = appUserRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.clinicRoomRepository = clinicRoomRepository;
        this.visitRepository = visitRepository;
        this.passwordEncoder = passwordEncoder;
        this.visitTypeRepository=visitTypeRepository;
    }

    // ========== DASHBOARD ==========

    @GetMapping
    public String adminDashboard(Model model) {

        long userCount = appUserRepository.count();
        long patientCount = patientRepository.count();
        long doctorCount = doctorRepository.count();
        long roomCount = clinicRoomRepository.count();
        long visitCount = visitRepository.count();
        long visitTypeCount=visitTypeRepository.count();

        model.addAttribute("userCount", userCount);
        model.addAttribute("patientCount", patientCount);
        model.addAttribute("doctorCount", doctorCount);
        model.addAttribute("roomCount", roomCount);
        model.addAttribute("visitCount", visitCount);
        model.addAttribute("visitTypeCount", visitTypeCount);


        return "admin/dashboard";
    }



    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", appUserRepository.findAll());
        return "admin/users-list";
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "admin/user-form";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        model.addAttribute("user", user);
        return "admin/user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(@RequestParam(required = false) Long id,
                           @RequestParam String username,
                           @RequestParam(required = false) String password,
                           @RequestParam String role,
                           @RequestParam(required = false) String patientCf) {

        AppUser user;
        if (id != null) {
            user = appUserRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        } else {
            user = new AppUser();
        }

        user.setUsername(username);
        user.setRole(Role.PATIENT);

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        } else if (user.getPassword() == null) {
            // new user without password -> not allowed, but for safety set empty encoded
            user.setPassword(passwordEncoder.encode("changeme"));
        }

        if (patientCf != null && !patientCf.isBlank()) {
            patientRepository.findById(patientCf)
                    .ifPresent(user::setPatient);
        } else {
            user.setPatient(null);
        }

        appUserRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        appUserRepository.deleteById(id);
        return "redirect:/admin/users";
    }



    @GetMapping("/patients")
    public String listPatients(Model model) {
        model.addAttribute("patients", patientRepository.findAll());
        return "admin/patients-list";
    }

    @GetMapping("/patients/new")
    public String newPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("visits", visitRepository.findAll()); // tutte le visite selezionabili
        return "admin/patient-form";
    }

    @GetMapping("/patients/edit/{cf}")
    public String editPatientForm(@PathVariable String cf, Model model) {
        Patient patient = patientRepository.findById(cf)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + cf));
        model.addAttribute("patient", patient);
        model.addAttribute("visits", visitRepository.findAll()); // tutte le visite selezionabili
        return "admin/patient-form";
    }
    @PostMapping("/patients/save")
    public String savePatient(@ModelAttribute("patient") Patient patient,
                              @RequestParam(value = "visitCodes", required = false) List<Long> visitCodes) {

        // se esiste, carico entity gestita; altrimenti nuova
        Patient managed = patient.getCf() != null
                ? patientRepository.findById(patient.getCf()).orElse(new Patient())
                : new Patient();


        managed.setCf(patient.getCf());
        managed.setFirstName(patient.getFirstName());
        managed.setSurname(patient.getSurname());
        managed.setAddress(patient.getAddress());
        managed.setBirthDate(patient.getBirthDate());


        // 1) rimuovo il paziente dalle vecchie visite
        for (Visit v : new HashSet<>(managed.getBookedVisits())) {
            v.getPatients().remove(managed);
            visitRepository.save(v);
        }
        managed.getBookedVisits().clear();

        // 2) aggiungo le nuove visite selezionate
        if (visitCodes != null) {
            for (Long code : visitCodes) {
                visitRepository.findById(code).ifPresent(v -> {
                    managed.getBookedVisits().add(v);
                    v.getPatients().add(managed);
                    visitRepository.save(v);
                });
            }
        }

        patientRepository.save(managed);
        return "redirect:/admin/patients";
    }


    @PostMapping("/patients/{cf}/delete")
    public String deletePatient(@PathVariable String cf) {
        patientRepository.deleteById(cf);
        return "redirect:/admin/patients";
    }



    @GetMapping("/doctors")
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorRepository.findAll());
        return "admin/doctors-list";
    }
    @GetMapping("/doctors/new")
    public String newDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("rooms", clinicRoomRepository.findAll());
        return "admin/doctor-form";
    }

    @GetMapping("/doctors/edit/{cf}")
    public String editDoctorForm(@PathVariable String cf, Model model) {
        Doctor doctor = doctorRepository.findById(cf)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + cf));
        model.addAttribute("doctor", doctor);
        model.addAttribute("rooms", clinicRoomRepository.findAll());
        return "admin/doctor-form";
    }


    @GetMapping("/visit-types")
    public String listVisitTypes(Model model) {
        model.addAttribute("visitTypes", visitTypeRepository.findAll());
        return "admin/visit-type"; // lista
    }

    @GetMapping("/visit-types/new")
    public String newVisitType(Model model) {
        model.addAttribute("visitType", new VisitType());
        return "admin/visit-type-form";
    }

    @GetMapping("/visit-types/edit/{id}")
    public String editVisitType(@PathVariable Long id,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        VisitType vt = visitTypeRepository.findById(id).orElse(null);
        if (vt == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Visit type not found.");
            return "redirect:/admin/visit-types";
        }

        model.addAttribute("visitType", vt);
        return "admin/visit-type-form";
    }

    @PostMapping("/visit-types/save")
    public String saveVisitType(@ModelAttribute VisitType visitType,
                                RedirectAttributes redirectAttributes) {

        boolean isNew = (visitType.getId() == null);
        visitTypeRepository.save(visitType);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                isNew ? "Visit type created." : "Visit type updated."
        );

        return "redirect:/admin/visit-types";
    }

    @PostMapping("/visit-types/{id}/delete")
    public String deleteVisitType(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {

        if (visitTypeRepository.existsById(id)) {
            visitTypeRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Visit type deleted.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Visit type not found.");
        }

        return "redirect:/admin/visit-types";
    }

    @PostMapping("/doctors/save")
    public String saveDoctor(
            @ModelAttribute("doctor") Doctor doctor,
            @RequestParam(value = "roomIds", required = false) List<Long> roomIds
    ) {
        Doctor managed;

        if (doctor.getCf() != null) {
            managed = doctorRepository.findById(doctor.getCf())
                    .orElse(new Doctor());
        } else {
            managed = new Doctor();
        }

        // basic field
        managed.setCf(doctor.getCf());
        managed.setFirstName(doctor.getFirstName());
        managed.setSurname(doctor.getSurname());
        managed.setEmail(doctor.getEmail());
        managed.setBirthDate(doctor.getBirthDate());
        managed.setSpecialization(doctor.getSpecialization());


        managed.getClinicRooms().clear();

        if (roomIds != null) {
            for (Long id : roomIds) {
                clinicRoomRepository.findById(id).ifPresent(room -> {
                    managed.getClinicRooms().add(room);   // owning side
                    room.getDoctors().add(managed);       // inverse side
                    clinicRoomRepository.save(room);
                });
            }
        }

        doctorRepository.save(managed);

        return "redirect:/admin/doctors";
    }



    @GetMapping("/rooms")
    public String listRooms(Model model) {
        model.addAttribute("rooms", clinicRoomRepository.findAll());
        return "admin/rooms-list";
    }

    @GetMapping("/rooms/new")
    public String newRoomForm(Model model) {
        model.addAttribute("room", new ClinicRoom());
        return "admin/room-form";
    }

    @GetMapping("/rooms/edit/{id}")
    public String editRoomForm(@PathVariable Long id, Model model) {
        ClinicRoom room = clinicRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
        model.addAttribute("room", room);
        return "admin/room-form";
    }

    @PostMapping("/rooms/save")
    public String saveRoom(
            @ModelAttribute("room") ClinicRoom room,
            @RequestParam(value = "doctorCfs", required = false) List<String> doctorCfs
    ) {

        ClinicRoom managed;


        if (room.getId() != null) {
            managed = clinicRoomRepository.findById(room.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Room not found: " + room.getId()));
        } else {
            managed = new ClinicRoom();
        }


        managed.setAddress(room.getAddress());


        managed = clinicRoomRepository.save(managed);

        for (Doctor d : new HashSet<>(managed.getDoctors())) {
            d.getClinicRooms().remove(managed);
            doctorRepository.save(d);
        }

        managed.getDoctors().clear();


        if (doctorCfs != null) {
            for (String cf : doctorCfs) {
                Optional<Doctor> od = doctorRepository.findById(cf);
                if (od.isPresent()) {
                    Doctor d = od.get();

                    // owning side
                    d.getClinicRooms().add(managed);

                    doctorRepository.save(d);
                }
            }
        }


        clinicRoomRepository.save(managed);

        return "redirect:/admin/rooms";
    }



    @PostMapping("/rooms/{id}/delete")
    public String deleteRoom(@PathVariable Long id) {
        clinicRoomRepository.deleteById(id);
        return "redirect:/admin/rooms";
    }



    @GetMapping("/visits")
    public String listVisits(Model model) {
        model.addAttribute("visits", visitRepository.findAll());
        return "admin/visits-list";
    }

    @GetMapping("/visits/new")
    public String showCreateVisitForm(Model model) {
        model.addAttribute("visit", new Visit());
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("visitTypes", visitTypeRepository.findAll());
        return "admin/visit-form";
    }

    @GetMapping("/visits/edit/{id}")
    public String showEditVisitForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid visit id: " + id));

        model.addAttribute("visit", visit);
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("visitTypes", visitTypeRepository.findAll());
        return "admin/visit-form";
    }

    @PostMapping("/visits/save")
    public String saveVisit(
            @ModelAttribute("visit") Visit visit,
            @RequestParam(value = "doctorCfs", required = false) List<String> doctorCfs,
            @RequestParam(required = false) Long visitTypeId,
            RedirectAttributes ra
    ) {

        if (visitTypeId != null) {
            visit.setVisitType(visitTypeRepository.findById(visitTypeId).orElse(null));
        } else {
            visit.setVisitType(null);
        }

        Visit managed;


        if (visit.getId() != null) {
            managed = visitRepository.findById(visit.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Visit not found: " + visit.getId()));
        } else {
            managed = new Visit();
        }


        managed.setDate(visit.getDate());

        managed = visitRepository.save(managed);


        for (Doctor d : new HashSet<>(managed.getDoctors())) {
            d.getPerformedVisits().remove(managed);   // owning side
            doctorRepository.save(d);
        }
        managed.getDoctors().clear(); // lato inverso

        if (doctorCfs != null) {
            for (String cf : doctorCfs) {
                Optional<Doctor> od = doctorRepository.findById(cf);
                if (od.isPresent()) {
                    Doctor d = od.get();


                    d.getPerformedVisits().add(managed);
                    doctorRepository.save(d);


                    managed.getDoctors().add(d);
                }
            }
        }
        visitRepository.save(managed);
        ra.addFlashAttribute("successMessage", "Visit saved.");
        return "redirect:/admin/visits";
    }

    @PostMapping("/visits/{id}/delete")
    public String deleteVisit(@PathVariable Long id) {
        visitRepository.deleteById(id);
        return "redirect:/admin/visits";
    }
}
