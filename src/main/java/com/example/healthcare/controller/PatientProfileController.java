package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.PatientProfileUpdateDto;
import com.example.healthcare.service.PatientProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient-profiles")
@AllArgsConstructor
public class PatientProfileController {

    private final PatientProfileService patientProfileService;

    @PostMapping
    public String createPatientProfile(@RequestHeader("Authorization") String token) {
        patientProfileService.createPatientProfile(token);
        return "Patient profile created successfully!";
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> updatePatientProfile(
            @RequestBody PatientProfileUpdateDto dto,
            @CookieValue(name = "jwt", required = true) String token) {

        Long patientId = JwtUtils.extractUserIdFromToken(token);
        patientProfileService.updatePatientProfile(patientId, dto);
        ApiResponse<Void> response = new ApiResponse<>(true, "Patient profile created successfully!", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Suspend a patient
    @PutMapping("/suspend/{patientId}")
    public ResponseEntity<ApiResponse<?>> suspendPatient(@PathVariable Long patientId) {
        var updatedPatient = patientProfileService.suspendPatient(patientId);
        ApiResponse<?> response = new ApiResponse<>(true, "Patient suspended successfully", updatedPatient);
        return ResponseEntity.ok(response);
    }

    // Restore a suspended patient
    @PutMapping("/restore/{patientId}")
    public ResponseEntity<ApiResponse<?>> restorePatient(@PathVariable Long patientId) {
        var updatedPatient = patientProfileService.restorePatient(patientId);
        ApiResponse<?> response = new ApiResponse<>(true, "Patient restored successfully", updatedPatient);
        return ResponseEntity.ok(response);
    }

}
