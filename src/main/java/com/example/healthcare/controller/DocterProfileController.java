package com.example.healthcare.controller;

import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor-profile")
@AllArgsConstructor
public class DocterProfileController {

    private final DoctorProfileService doctorProfileService;
    private final JwtUtils jwtUtils;

    @PostMapping("/create-doctor-profile")
    public ResponseEntity<String> createDoctorProfile(
            @RequestBody String token) {
        doctorProfileService.createDoctorProfile(token);
        return ResponseEntity.ok("Doctor profile created successfully!");
    }

    @PutMapping("/update-doctor-profile")
    public ResponseEntity<String> updateDoctorProfile(
            @RequestBody DoctorProfileUpdateDto dto,
            @CookieValue(name = "jwt", required = true) String token){ // read cookie

        Long doctorId = JwtUtils.extractUserIdFromToken(token);
        doctorProfileService.updateDoctorProfile(doctorId, dto);
        return ResponseEntity.ok("Doctor profile updated successfully!");
    }

}
