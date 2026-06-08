// controller/PatientDoctorController.java
package com.example.healthcare.doctor.controller;

import com.example.healthcare.doctor.service.PatientDoctorService;
import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.doctor.dto.DoctorProfileResponseDto;
import com.example.healthcare.doctor.dto.DoctorWithScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments/patient/doctors")
@RequiredArgsConstructor
public class PatientDoctorController {

    private final PatientDoctorService patientDoctorService;

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<DoctorWithScheduleDto>>> getAllAvailableDoctors() {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Available doctors fetched successfully",
                patientDoctorService.getAllAvailableDoctors()));
    }

    @GetMapping("/specialization")
    public ResponseEntity<ApiResponse<List<DoctorProfileResponseDto>>> getBySpecialization(
            @RequestParam String specialization) {

        return ResponseEntity.ok(ApiResponse.<List<DoctorProfileResponseDto>>builder()
                .status(true)
                .message("Doctors fetched successfully")
                .data(patientDoctorService.getSpecializedDoctorProfile(specialization))
                .build());
    }

    @GetMapping("/experience")
    public ResponseEntity<ApiResponse<List<DoctorProfileResponseDto>>> getByExperience(
            @RequestParam String level) {

        return ResponseEntity.ok(ApiResponse.<List<DoctorProfileResponseDto>>builder()
                .status(true)
                .message("Doctors fetched successfully")
                .data(patientDoctorService.getDoctorsByExperience(level))
                .build());
    }
}
