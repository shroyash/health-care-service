package com.example.healthcare.controller;

import com.example.healthcare.dto.response.ApiResponse;
import com.example.healthcare.dto.response.PatientProfileDTO;
import com.example.healthcare.service.AdminPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/patients")
@RequiredArgsConstructor
public class AdminPatientController {

    private final AdminPatientService adminPatientService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientProfileDTO>>> getAllPatients() {

        return ResponseEntity.ok(ApiResponse.<List<PatientProfileDTO>>builder()
                .status(true)
                .message("Patients fetched successfully")
                .data(adminPatientService.getAllPatients())
                .build());
    }
}