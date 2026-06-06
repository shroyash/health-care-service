package com.example.healthcare.doctor.controller;

import com.example.healthcare.common.service.FileStorageService;
import com.example.healthcare.doctor.service.DoctorProfileService;
import com.example.healthcare.doctor.dto.DoctorProfileUpdateDto;
import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.doctor.dto.DoctorProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctor-profiles")
@RequiredArgsConstructor
public class DoctorProfileController {

    private final DoctorProfileService doctorProfileService;
    private final FileStorageService fileStorageService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<DoctorProfileResponseDto>> getDoctorProfile(
            @RequestHeader("X-User-Id") UUID userId) {

        return ResponseEntity.ok(ApiResponse.<DoctorProfileResponseDto>builder()
                .status(true)
                .message("Doctor profile fetched successfully")
                .data(doctorProfileService.getDoctorProfile(userId))
                .build());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateDoctorProfile(
            @RequestBody DoctorProfileUpdateDto dto,
            @RequestHeader("X-User-Id") UUID userId) {

        doctorProfileService.updateDoctorProfile(userId, dto);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(true)
                .message("Doctor profile updated successfully")
                .data(null)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorProfileResponseDto>>> getAllActiveDoctors() {
        return ResponseEntity.ok(ApiResponse.<List<DoctorProfileResponseDto>>builder()
                .status(true)
                .message("Active doctors fetched successfully")
                .data(doctorProfileService.getAllActiveDoctors())
                .build());
    }

    @GetMapping("/specialization")
    public ResponseEntity<ApiResponse<List<DoctorProfileResponseDto>>> getDoctorsBySpecialization(
            @RequestParam String specialization) {
        return ResponseEntity.ok(ApiResponse.<List<DoctorProfileResponseDto>>builder()
                .status(true)
                .message("Doctors fetched successfully")
                .data(doctorProfileService.getSpecializedDoctorProfile(specialization))
                .build());
    }

    @GetMapping("/experience")
    public ResponseEntity<ApiResponse<List<DoctorProfileResponseDto>>> getDoctorsByExperience(
            @RequestParam String level) {
        return ResponseEntity.ok(ApiResponse.<List<DoctorProfileResponseDto>>builder()
                .status(true)
                .message("Doctors fetched successfully")
                .data(doctorProfileService.getDoctorsByExperience(level))
                .build());
    }

    @PostMapping("/upload-img")
    public ResponseEntity<ApiResponse<String>> uploadProfileImg(
            @RequestHeader("X-User-Id") UUID doctorId,
            @RequestParam("file") MultipartFile file) {

        String fileUrl = fileStorageService.saveFile(file);
        doctorProfileService.updateProfileImage(doctorId, fileUrl);

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .status(true)
                .message("Doctor profile image uploaded successfully")
                .data(fileUrl)
                .build());
    }
}