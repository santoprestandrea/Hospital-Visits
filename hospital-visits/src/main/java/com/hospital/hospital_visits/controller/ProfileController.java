package com.hospital.hospital_visits.controller;

import com.hospital.hospital_visits.dto.ChangePasswordForm;
import com.hospital.hospital_visits.dto.UpdateProfileImageForm;
import com.hospital.hospital_visits.entity.AppUser;
import com.hospital.hospital_visits.security.CustomUserDetails;
import com.hospital.hospital_visits.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
/*
*
* This controller allows us to control the behavior of web pages
* developed using HTML templates and manage all the operations that
* can be performed here
*
* */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        AppUser u = userDetails.getAppUser();

        UpdateProfileImageForm imageForm = new UpdateProfileImageForm();
        imageForm.setProfileImageUrl(u.getProfileImageUrl());

        model.addAttribute("user", u);
        model.addAttribute("imageForm", imageForm);
        model.addAttribute("passwordForm", new ChangePasswordForm());

        return "profile";
    }

    @PostMapping("/profile-image")
    public String updateProfileImage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @Valid @ModelAttribute("imageForm") UpdateProfileImageForm imageForm,
                                     BindingResult br,
                                     Model model) {
        if (br.hasErrors()) {
            model.addAttribute("user", userDetails.getAppUser());
            model.addAttribute("passwordForm", new ChangePasswordForm());
            return "profile";
        }

        userService.updateMyProfileImage(userDetails.getAppUser().getId(),
                toRequest(imageForm));

        return "redirect:/profile?imageUpdated";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @Valid @ModelAttribute("passwordForm") ChangePasswordForm passwordForm,
                                 BindingResult br,
                                 Model model) {
        if (br.hasErrors()) {
            model.addAttribute("user", userDetails.getAppUser());
            UpdateProfileImageForm imageForm = new UpdateProfileImageForm();
            imageForm.setProfileImageUrl(userDetails.getAppUser().getProfileImageUrl());
            model.addAttribute("imageForm", imageForm);
            return "profile";
        }

        try {
            userService.changeMyPassword(
                    userDetails.getAppUser().getId(),
                    passwordForm.getCurrentPassword(),
                    passwordForm.getNewPassword(),
                    passwordForm.getConfirmNewPassword()
            );
        } catch (RuntimeException ex) {
            model.addAttribute("user", userDetails.getAppUser());
            UpdateProfileImageForm imageForm = new UpdateProfileImageForm();
            imageForm.setProfileImageUrl(userDetails.getAppUser().getProfileImageUrl());
            model.addAttribute("imageForm", imageForm);
            model.addAttribute("passwordError", ex.getMessage());
            return "profile";
        }

        return "redirect:/profile?passwordUpdated";
    }


    private com.hospital.hospital_visits.dto.UpdateProfileImageRequest toRequest(UpdateProfileImageForm form) {
        com.hospital.hospital_visits.dto.UpdateProfileImageRequest req =
                new com.hospital.hospital_visits.dto.UpdateProfileImageRequest();
        req.setProfileImageUrl(form.getProfileImageUrl());
        return req;
    }
}
