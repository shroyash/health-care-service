package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.DoctorWithScheduleDto;
import com.example.healthcare.dto.PatientAppointmentDto;
import com.example.healthcare.dto.PatientDashboardStatsDto;
import com.example.healthcare.service.PatientDashboardService;
import com.example.healthcare.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard/patient")
public class PatientDashboardController {

    private final PatientDashboardService patientDashboardService;

    public PatientDashboardController(PatientDashboardService patientDashboardService) {
        this.patientDashboardService = patientDashboardService;
    }

    @GetMapping("/appointments/upcoming")
    public ResponseEntity<ApiResponse<PatientDashboardStatsDto>> getUpcomingAppointments(@CookieValue("jwt") String token) {
        long userId = JwtUtils.extractUserIdFromToken(token);
        long totalUpcoming = patientDashboardService.getTotalUpcomingAppointments(userId);
        List<PatientAppointmentDto> appointments = patientDashboardService.getUpcomingAppointments(userId);

        PatientDashboardStatsDto data = new PatientDashboardStatsDto(totalUpcoming, appointments);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Upcoming confirmed appointments fetched successfully", data)
        );
    }

    @GetMapping("/available-doctor")
    public ResponseEntity<ApiResponse<List<DoctorWithScheduleDto>>> getAllAvailableDoctor(){

        List<DoctorWithScheduleDto> availableDoctor = patientDashboardService.getAllAvailableDoctor();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Upcoming confirmed appointments fetched successfully", availableDoctor)
        );


    }
}
