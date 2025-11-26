package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.PatientProfileUpdateDto;
import com.example.healthcare.service.FileStorageService;
import com.example.healthcare.service.PatientProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/patient-profiles")
@AllArgsConstructor
public class PatientProfileController {

    private final PatientProfileService patientProfileService;
    private final FileStorageService fileStorageService;

    // Create patient profile
    @PostMapping
    public String createPatientProfile(@RequestHeader("Authorization") String token) {
        patientProfileService.createPatientProfile(token);
        return "Patient profile created successfully!";
    }

    // Update patient profile info
    @PutMapping
    public ResponseEntity<ApiResponse<?>> updatePatientProfile(
            @RequestBody PatientProfileUpdateDto dto,
            @CookieValue(name = "jwt", required = true) String token) {

        Long patientId = JwtUtils.extractUserIdFromToken(token);
        patientProfileService.updatePatientProfile(patientId, dto);
        ApiResponse<Void> response = new ApiResponse<>(true, "Patient profile updated successfully!", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Upload patient profile image
    @PostMapping("/{patientId}/upload-img")
    public ResponseEntity<ApiResponse<String>> uploadProfileImg(
            @PathVariable Long patientId,
            @RequestParam("file") MultipartFile file) {

            String fileUrl = fileStorageService.saveFile(file);

            patientProfileService.updateProfileImage(patientId, fileUrl);

            ApiResponse<String> response = ApiResponse.<String>builder()
                    .status(true)
                    .message("Patient profile image uploaded successfully")
                    .data(fileUrl)
                    .build();

            return ResponseEntity.ok(response);
    }
}
