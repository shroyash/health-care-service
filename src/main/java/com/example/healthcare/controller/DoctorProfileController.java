package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.DoctorProfileResponseDto;
import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.service.FileStorageService;
import com.example.healthcare.utils.JwtUtils;
import jakarta.ws.rs.CookieParam;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctor-profiles")
@AllArgsConstructor
public class DoctorProfileController {

    private final DoctorProfileService doctorProfileService;
    private final FileStorageService fileStorageService;


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<DoctorProfileResponseDto>> getDoctorProfile(
            @CookieValue("jwt") String token) {

        UUID userId = JwtUtils.extractUserIdFromToken(token);

        DoctorProfileResponseDto doctorProfile =
                doctorProfileService.getDoctorProfile(userId);

        ApiResponse<DoctorProfileResponseDto> response = ApiResponse.<DoctorProfileResponseDto>builder()
                .status(true)
                .message("Doctor profile fetched successfully")
                .data(doctorProfile)
                .build();

        return ResponseEntity.ok(response);
    }


    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateDoctorProfile(
            @RequestBody DoctorProfileUpdateDto dto,
            @CookieValue(name = "jwt", required = true) String token) { // read cookie

        UUID userId = JwtUtils.extractUserIdFromToken(token);
        doctorProfileService.updateDoctorProfile(userId, dto);

        ApiResponse<Void> response = new ApiResponse<>(true, "Doctor profile updated successfully!", null);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorProfileResponseDto>>> getAllDoctorProfile() {
        List<DoctorProfileResponseDto> doctors = doctorProfileService.getAllDoctorProfiles();

        ApiResponse<List<DoctorProfileResponseDto>> response = new ApiResponse<>(
                true,
                "Doctors fetched successfully",
                doctors
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/specialization")
    public ResponseEntity<ApiResponse<List<DoctorProfileResponseDto>>> getSpecializedDoctorProfile(
            @RequestParam("specialization") String specialization) {

        List<DoctorProfileResponseDto> specializationDoctors = doctorProfileService.getSpecializedDoctorProfile(specialization);

        ApiResponse<List<DoctorProfileResponseDto>> response = ApiResponse.<List<DoctorProfileResponseDto>>builder()
                .status(true)
                .message("Specialized doctors fetched successfully")
                .data(specializationDoctors)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/experience")
    public ResponseEntity<ApiResponse<List<DoctorProfileResponseDto>>> getDoctorsByExperience(
            @RequestParam("level") String level) {

        List<DoctorProfileResponseDto> doctors = doctorProfileService.getDoctorsByExperience(level);

        ApiResponse<List<DoctorProfileResponseDto>> response = ApiResponse.<List<DoctorProfileResponseDto>>builder()
                .status(true)
                .message("Doctors fetched successfully for experience level: " + level)
                .data(doctors)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{doctorId}/upload-img")
    public ResponseEntity<ApiResponse<String>> uploadProfileImg(
            @PathVariable UUID doctorId,
            @RequestParam("file") MultipartFile file) {
            String fileUrl = fileStorageService.saveFile(file);

            doctorProfileService.updateProfileImage(doctorId, fileUrl);

            ApiResponse<String> response = ApiResponse.<String>builder()
                    .status(true)
                    .message("Doctor profile image uploaded successfully")
                    .data(fileUrl)
                    .build();

            return ResponseEntity.ok(response);

        }

}
