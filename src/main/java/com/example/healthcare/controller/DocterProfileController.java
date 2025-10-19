package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.DoctorProfileResponseDto;
import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-profiles")
@AllArgsConstructor
public class DocterProfileController {

    private final DoctorProfileService doctorProfileService;

    @PostMapping
    public String createDoctorProfile(@RequestHeader("Authorization") String token) {
        doctorProfileService.createDoctorProfile(token);
        return "Doctor profile created successfully!";
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateDoctorProfile(
            @RequestBody DoctorProfileUpdateDto dto,
            @CookieValue(name = "jwt", required = true) String token) { // read cookie

        Long doctorId = JwtUtils.extractUserIdFromToken(token);
        doctorProfileService.updateDoctorProfile(doctorId, dto);

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



}
