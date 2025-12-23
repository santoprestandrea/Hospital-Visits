package com.hospital.hospital_visits.service;

import com.hospital.hospital_visits.dto.UpdateProfileImageRequest;
import com.hospital.hospital_visits.dto.UserDTO;
import com.hospital.hospital_visits.entity.AppUser;
import com.hospital.hospital_visits.exception.BadRequestException;
import com.hospital.hospital_visits.exception.ResourceNotFoundException;
import com.hospital.hospital_visits.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserDTO toDTO(AppUser u) {
        String patientCf = (u.getPatient() != null) ? u.getPatient().getCf() : null;
        String doctorCf  = (u.getDoctor()  != null) ? u.getDoctor().getCf()  : null;

        return new UserDTO(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole(),
                u.getProfileImageUrl(),
                u.getRegistrationDate(),
                patientCf,
                doctorCf
        );
    }

    public UserDTO getById(Long id) {
        AppUser u = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return toDTO(u);
    }

    public UserDTO updateMyProfileImage(Long userId, UpdateProfileImageRequest request) {
        AppUser u = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        u.setProfileImageUrl(request.getProfileImageUrl());
        u = appUserRepository.save(u);

        return toDTO(u);
    }

    public void changeMyPassword(Long userId, String currentPassword, String newPassword, String confirmNewPassword) {
        AppUser u = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        if (!passwordEncoder.matches(currentPassword, u.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new BadRequestException("New password and confirmation do not match");
        }

        u.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(u);
    }
}
