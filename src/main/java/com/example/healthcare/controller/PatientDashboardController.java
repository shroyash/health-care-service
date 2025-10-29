package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.PatientAppointmentDto;
import com.example.healthcare.dto.PatientDashboardStatsDto;
import com.example.healthcare.service.PatientDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard/patient")
public class PatientDashboardController {

    private final PatientDashboardService patientDashboardService;

    public PatientDashboardController(PatientDashboardService patientDashboardService) {
        this.patientDashboardService = patientDashboardService;
    }

    @GetMapping("/appointments/upcoming/{patientId}")
    public ResponseEntity<ApiResponse<PatientDashboardStatsDto>> getUpcomingAppointments(@PathVariable Long patientId) {
        long totalUpcoming = patientDashboardService.getTotalUpcomingAppointments(patientId);
        List<PatientAppointmentDto> appointments = patientDashboardService.getUpcomingAppointments(patientId);

        PatientDashboardStatsDto data = new PatientDashboardStatsDto(totalUpcoming, appointments);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Upcoming confirmed appointments fetched successfully", data)
        );
    }

}
