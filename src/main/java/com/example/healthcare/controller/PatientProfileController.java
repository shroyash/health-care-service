package com.example.healthcare.controller;

import com.example.healthcare.dto.PatientProfileUpdateDto;
import com.example.healthcare.service.PatientProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient-profile")
@AllArgsConstructor
public class PatientProfileController {

    private final PatientProfileService patientProfileService;

    // Create patient profile
    @PostMapping("/create")
    public ResponseEntity<String> createPatientProfile(@RequestBody String token) {
        patientProfileService.createPatientProfile(token);
        return ResponseEntity.ok("Patient profile created successfully!");
    }

    // Update patient profile
    @PutMapping("/update")
    public ResponseEntity<String> updatePatientProfile(
            @RequestBody PatientProfileUpdateDto dto,
            @CookieValue(name = "jwt", required = true) String token) {

        Long patientId = JwtUtils.extractUserIdFromToken(token);
        patientProfileService.updatePatientProfile(patientId, dto);
        return ResponseEntity.ok("Patient profile updated successfully!");
    }
}
