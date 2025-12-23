package com.hospital.hospital_visits.controller.api;

import com.hospital.hospital_visits.dto.ChangePasswordRequest;
import com.hospital.hospital_visits.dto.UpdateProfileImageRequest;
import com.hospital.hospital_visits.dto.UserDTO;
import com.hospital.hospital_visits.security.CustomUserDetails;
import com.hospital.hospital_visits.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
/*

* class that allows me to interface with the code via postman and perform some APIs,
* through the class repository and the corresponding data transfer object (dto)
*
* */
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserDTO me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getById(userDetails.getAppUser().getId());
    }

    @PatchMapping("/me/profile-image")
    @PreAuthorize("isAuthenticated()")
    public UserDTO updateMyProfileImage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @Valid @RequestBody UpdateProfileImageRequest request) {
        return userService.updateMyProfileImage(userDetails.getAppUser().getId(), request);
    }

    @PatchMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public void changeMyPassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @Valid @RequestBody ChangePasswordRequest request) {
        userService.changeMyPassword(
                userDetails.getAppUser().getId(),
                request.getCurrentPassword(),
                request.getNewPassword(),
                request.getConfirmNewPassword()
        );
    }
}
