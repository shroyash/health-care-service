
package com.example.healthcare.dashboard.controller;

import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.dashboard.dto.PatientDashboardStatsDto;
import com.example.healthcare.dashboard.service.PatientDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard/patient")
@RequiredArgsConstructor
public class PatientDashboardController {

    private final PatientDashboardService patientDashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<PatientDashboardStatsDto>> getDashboardStats(
            @RequestHeader("X-User-Id") UUID patientId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Patient dashboard stats fetched successfully",
                patientDashboardService.getDashboardStats(patientId)));
    }
}