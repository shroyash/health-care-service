package com.example.healthcare.patient.controller;

import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.patient.dto.PatientProfileDTO;
import com.example.healthcare.patient.service.AdminPatientService;
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