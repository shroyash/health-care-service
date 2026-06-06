// dashboard/controller/DoctorDashboardController.java
package com.example.healthcare.dashboard.controller;

import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.dashboard.dto.DoctorDashboardStatsDto;
import com.example.healthcare.dashboard.service.DoctorDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard/doctor")
@RequiredArgsConstructor
public class DoctorDashboardController {

    private final DoctorDashboardService doctorDashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DoctorDashboardStatsDto>> getDashboardStats(
            @RequestHeader("X-User-Id") UUID doctorId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Doctor dashboard stats fetched successfully",
                doctorDashboardService.getDashboardStats(doctorId)));
    }
}