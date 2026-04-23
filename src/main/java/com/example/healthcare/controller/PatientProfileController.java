package com.example.healthcare.controller;

import com.example.healthcare.dto.request.PatientProfileUpdateDto;
import com.example.healthcare.dto.response.ApiResponse;
import com.example.healthcare.dto.response.PatientProfileDTO;
import com.example.healthcare.service.FileStorageService;
import com.example.healthcare.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/patient-profiles")
@RequiredArgsConstructor
public class PatientProfileController {

    private final PatientProfileService patientProfileService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<ApiResponse<PatientProfileDTO>> getPatientProfile(
            @RequestHeader("X-User-Id") UUID userId) {

        return ResponseEntity.ok(ApiResponse.<PatientProfileDTO>builder()
                .status(true)
                .message("Patient profile fetch successful")
                .data(patientProfileService.getPatientProfile(userId))
                .build());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updatePatientProfile(
            @RequestBody PatientProfileUpdateDto dto,
            @RequestHeader("X-User-Id") UUID patientId) {

        patientProfileService.updatePatientProfile(patientId, dto);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(true)
                .message("Patient profile updated successfully")
                .data(null)
                .build());
    }

    @PostMapping("/upload-img")
    public ResponseEntity<ApiResponse<String>> uploadProfileImg(
            @RequestHeader("X-User-Id") UUID patientId,
            @RequestParam("file") MultipartFile file) {

        String fileUrl = fileStorageService.saveFile(file);
        patientProfileService.updateProfileImage(patientId, fileUrl);

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .status(true)
                .message("Patient profile image uploaded successfully")
                .data(fileUrl)
                .build());
    }
}