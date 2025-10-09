package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-profiles")
@AllArgsConstructor
public class DocterProfileController {

    private final DoctorProfileService doctorProfileService;

    @PostMapping
    public String createDoctorProfile(
            @RequestBody String token) {
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
    public ResponseEntity<ApiResponse<List<DoctorProfile>>> getAllDoctorProfile() {
        List<DoctorProfile> doctors = doctorProfileService.getAllDoctorProfile();

        ApiResponse<List<DoctorProfile>> response = new ApiResponse<>(
                true,
                "Doctors fetched successfully",
                doctors
        );

        return ResponseEntity.ok(response);
    }


}
