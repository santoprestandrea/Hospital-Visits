package com.hospital.hospital_visits.config;

import com.hospital.hospital_visits.entity.*;
import com.hospital.hospital_visits.repository.AppUserRepository;
import com.hospital.hospital_visits.repository.ClinicRoomRepository;
import com.hospital.hospital_visits.repository.DepartmentRepository;
import com.hospital.hospital_visits.repository.DoctorRepository;
import com.hospital.hospital_visits.repository.PatientRepository;
import com.hospital.hospital_visits.repository.SpecializationRepository;
import com.hospital.hospital_visits.repository.VisitRepository;
import com.hospital.hospital_visits.repository.VisitTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/*

* class to initialize the default database data,
* creates the data if the database is not already
* populated and via hibernate (application.proterties)
* prints the results of various operations to the console,
* such as successful user insertion

* */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(DoctorRepository doctorRepository,
                                      PatientRepository patientRepository,
                                      ClinicRoomRepository clinicRoomRepository,
                                      VisitRepository visitRepository,
                                      AppUserRepository appUserRepository,
                                      DepartmentRepository departmentRepository,
                                      SpecializationRepository specializationRepository,
                                      VisitTypeRepository visitTypeRepository,
                                      PasswordEncoder passwordEncoder) {

        return args -> {

            if (doctorRepository.count() == 0
                    && patientRepository.count() == 0
                    && clinicRoomRepository.count() == 0
                    && visitRepository.count() == 0) {


                Department cardiology = new Department();
                cardiology.setName("Cardiology");
                cardiology.setDescription("Heart-related treatments");

                Department neurology = new Department();
                neurology.setName("Neurology");
                neurology.setDescription("Brain and nervous system");

                departmentRepository.saveAll(List.of(cardiology, neurology));


                Specialization cardioSpec = new Specialization();
                cardioSpec.setName("Cardiologist");
                cardioSpec.setDescription("Heart specialist");

                Specialization neuroSpec = new Specialization();
                neuroSpec.setName("Neurologist");
                neuroSpec.setDescription("Nervous system specialist");

                specializationRepository.saveAll(List.of(cardioSpec, neuroSpec));

                VisitType cardioCheck = new VisitType();
                cardioCheck.setName("Cardiology Check");
                cardioCheck.setDescription("General cardiology visit");

                VisitType neuroConsult = new VisitType();
                neuroConsult.setName("Neurology Consultation");
                neuroConsult.setDescription("Consultation for neurological issues");

                visitTypeRepository.saveAll(List.of(cardioCheck, neuroConsult));


                Doctor doc1 = new Doctor();
                doc1.setCf("DRMARIO12345678");
                doc1.setFirstName("Mario");
                doc1.setSurname("Rossi");
                doc1.setEmail("mario.rossi@hospital.com");
                doc1.setBirthDate(LocalDate.of(1980, 5, 10));
                doc1.setDepartment(cardiology);
                doc1.setSpecialization(cardioSpec);

                Doctor doc2 = new Doctor();
                doc2.setCf("DRGIULIA123456");
                doc2.setFirstName("Giulia");
                doc2.setSurname("Bianchi");
                doc2.setEmail("giulia.bianchi@hospital.com");
                doc2.setBirthDate(LocalDate.of(1985, 3, 22));
                doc2.setDepartment(neurology);
                doc2.setSpecialization(neuroSpec);

                doctorRepository.saveAll(List.of(doc1, doc2));


                Patient pat1 = new Patient();
                pat1.setCf("PTASAIANA123456");
                pat1.setFirstName("Santo");
                pat1.setSurname("Smith");
                pat1.setAddress("Main Street 1, City");
                pat1.setBirthDate(LocalDate.of(2002, 7, 11));

                Patient pat2 = new Patient();
                pat2.setCf("PTANDREA1234567");
                pat2.setFirstName("Andrea");
                pat2.setSurname("Brown");
                pat2.setAddress("Second Street 5, City");
                pat2.setBirthDate(LocalDate.of(1999, 9, 18));

                patientRepository.saveAll(List.of(pat1, pat2));


                ClinicRoom room1 = new ClinicRoom();
                room1.setAddress("Cardiology - Building A, Floor 1");

                ClinicRoom room2 = new ClinicRoom();
                room2.setAddress("Neurology - Building B, Floor 2");

                clinicRoomRepository.saveAll(List.of(room1, room2));


                doc1.getClinicRooms().add(room1);
                room1.getDoctors().add(doc1);

                doc2.getClinicRooms().add(room2);
                room2.getDoctors().add(doc2);

                doctorRepository.saveAll(List.of(doc1, doc2));
                clinicRoomRepository.saveAll(List.of(room1, room2));


                Visit visit1 = new Visit();
                visit1.setDate(LocalDate.now().plusDays(1));
                visit1.setDepartment(cardiology);
                visit1.setVisitType(cardioCheck);

                Visit visit2 = new Visit();
                visit2.setDate(LocalDate.now().plusDays(2));
                visit2.setDepartment(neurology);
                visit2.setVisitType(neuroConsult);


                visitRepository.saveAll(List.of(visit1, visit2));


                doc1.getPerformedVisits().add(visit1);
                visit1.getDoctors().add(doc1);

                doc2.getPerformedVisits().add(visit2);
                visit2.getDoctors().add(doc2);


                pat1.getBookedVisits().add(visit1);
                visit1.getPatients().add(pat1);

                pat2.getBookedVisits().add(visit2);
                visit2.getPatients().add(pat2);

                doctorRepository.saveAll(List.of(doc1, doc2));
                patientRepository.saveAll(List.of(pat1, pat2));
                visitRepository.saveAll(List.of(visit1, visit2));

                System.out.println("Sample domain data initialized.");
            }


            // Technical patient for the "booking" user: if it's not there, I create it
            Patient bookingPatient = patientRepository.findById("PTBOOKING000000")
                    .orElseGet(() -> {
                        Patient p = new Patient();
                        p.setCf("PTBOOKING000000");
                        p.setFirstName("Booking");
                        p.setSurname("User");
                        p.setAddress("Default address");
                        p.setBirthDate(LocalDate.of(1990, 1, 1));
                        return patientRepository.save(p);
                    });


            appUserRepository.findByUsername("admin").orElseGet(() -> {
                AppUser adminUser = new AppUser();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@hospital.com");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setRole(Role.ADMIN);
                adminUser.setPatient(null);
                adminUser.setProfileImageUrl("https://example.com/default-admin-avatar.png");
                adminUser.setRegistrationDate(LocalDateTime.now());
                System.out.println("Created default admin user: admin/admin123");
                return appUserRepository.save(adminUser);
            });

            // USER "booking" - linked to bookingPatient
            appUserRepository.findByUsername("booking").orElseGet(() -> {
                AppUser bookingUser = new AppUser();
                bookingUser.setUsername("booking");
                bookingUser.setEmail("booking@hospital.com");
                bookingUser.setPassword(passwordEncoder.encode("booking123"));
                bookingUser.setRole(Role.PATIENT);
                bookingUser.setPatient(bookingPatient);
                bookingUser.setProfileImageUrl("https://example.com/default-user-avatar.png");
                bookingUser.setRegistrationDate(LocalDateTime.now());
                System.out.println("Created default booking user: booking/booking123");
                return appUserRepository.save(bookingUser);
            });
        };
    }
}
