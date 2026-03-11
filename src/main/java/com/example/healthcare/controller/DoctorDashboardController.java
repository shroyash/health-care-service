package com.example.healthcare.controller;

import com.example.healthcare.dto.response.*;
import com.example.healthcare.service.DoctorDashboardService;
import com.example.healthcare.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard/doctor")
@RequiredArgsConstructor
public class DoctorDashboardController {

    private final DoctorDashboardService doctorDashboardService;


    @GetMapping
    public ResponseEntity<ApiResponse<DoctorDashboardStatsDto>> getDoctorDashboard(
            @CookieValue("jwt") String token
    ) {
        UUID doctorId = JwtUtils.extractUserIdFromToken(token);
        DoctorDashboardStatsDto dashboard = doctorDashboardService.getDoctorDashboard(doctorId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Doctor dashboard data fetched successfully", dashboard)
        );
    }

    @GetMapping("/upcoming-appointments")
    public ResponseEntity<ApiResponse<?>> getUpcommingAppointments(
            @CookieValue("jwt") String token
    ) {
        UUID userId = JwtUtils.extractUserIdFromToken(token);
        var appointments = doctorDashboardService.getAppointments(userId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Today's appointments fetched successfully", appointments)
        );
    }

    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<List<?>>> getAppointments(@CookieValue("jwt") String token) {
        UUID userId = JwtUtils.extractUserIdFromToken(token);
        var appointments = doctorDashboardService.getAllAppointments(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "appointments fetched successfully", appointments)
        );
    }

    @GetMapping("/weekly-count")
    public ApiResponse<List<DailyAppointmentCountDto>> getDoctorWeeklyAppointmentCount(
            @CookieValue("jwt") String token
    ) {
        UUID doctorId = JwtUtils.extractUserIdFromToken(token);
        List<DailyAppointmentCountDto> dailyAppointmentCounts =
                doctorDashboardService.getDoctorWeeklyAppointments(doctorId);

        return new ApiResponse<>(
                true,
                "Weekly count for doctor fetched successfully",
                dailyAppointmentCounts
        );
    }

    @GetMapping("/checkup-count")
    public ApiResponse<List<CheckupTypeCountDto>> getAppointmentCountByCheckupType(
            @CookieValue("jwt") String token
    ) {

        UUID doctorId = JwtUtils.extractUserIdFromToken(token);

        List<CheckupTypeCountDto> counts =
                doctorDashboardService.getAppointmentCountByCheckupType(doctorId);

        return new ApiResponse<>(
                true,
                "Appointment counts by checkup type fetched successfully",
                counts
        );
    }

}
