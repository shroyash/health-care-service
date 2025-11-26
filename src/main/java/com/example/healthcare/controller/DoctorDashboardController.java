package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.DoctorDashboardStatsDto;
import com.example.healthcare.service.DoctorDashboardService;
import com.example.healthcare.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/doctor")
@RequiredArgsConstructor
public class DoctorDashboardController {

    private final DoctorDashboardService doctorDashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DoctorDashboardStatsDto>> getDoctorDashboard(
            @CookieValue("jwt") String token
    ) {
        Long doctorId = JwtUtils.extractUserIdFromToken(token);
        DoctorDashboardStatsDto dashboard = doctorDashboardService.getDoctorDashboard(doctorId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Doctor dashboard data fetched successfully", dashboard)
        );
    }

    @GetMapping("/today-appointments")
    public ResponseEntity<ApiResponse<?>> getTodayAppointments(
            @CookieValue("jwt") String token
    ) {
        Long userId = JwtUtils.extractUserIdFromToken(token);
        var appointments = doctorDashboardService.getAppointments(userId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Today's appointments fetched successfully", appointments)
        );
    }
}
