package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.DoctorDashboardStatsDto;
import com.example.healthcare.service.DoctorDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/doctor")
@RequiredArgsConstructor
public class DoctorDashboardController {
    private final DoctorDashboardService doctorDashboardService;

    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<DoctorDashboardStatsDto>> getDoctorDashboard(@PathVariable Long doctorId) {
        DoctorDashboardStatsDto dashboard = doctorDashboardService.getDoctorDashboard(doctorId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Doctor dashboard data fetched successfully", dashboard));
    }
}
