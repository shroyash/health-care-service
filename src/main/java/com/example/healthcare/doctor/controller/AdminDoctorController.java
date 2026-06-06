package com.example.healthcare.doctor.controller;

import com.example.healthcare.doctor.service.AdminDoctorService;
import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.doctor.dto.DoctorProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/admin/doctors")
@RequiredArgsConstructor
public class AdminDoctorController {

    private final AdminDoctorService doctorProfileService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorProfileResponseDto>>> getAllDoctors() {
        return ResponseEntity.ok(ApiResponse.<List<DoctorProfileResponseDto>>builder()
                .status(true)
                .message("All doctors fetched successfully")
                .data(doctorProfileService.getAllDoctors())
                .build());
    }

}