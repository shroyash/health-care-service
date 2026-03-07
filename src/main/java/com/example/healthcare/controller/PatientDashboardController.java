package com.example.healthcare.controller;

import com.example.healthcare.dto.response.ApiResponse;
import com.example.healthcare.dto.response.DoctorWithScheduleDto;
import com.example.healthcare.dto.response.PatientAppointmentDto;
import com.example.healthcare.dto.response.PatientDashboardStatsDto;
import com.example.healthcare.service.AdminDashboardStatusService;
import com.example.healthcare.service.PatientDashboardService;
import com.example.healthcare.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard/patient")
public class    PatientDashboardController {

    private final PatientDashboardService patientDashboardService;
    private final AdminDashboardStatusService adminDashboardStatusService;

    public PatientDashboardController(PatientDashboardService patientDashboardService, AdminDashboardStatusService adminDashboardStatusService) {
        this.patientDashboardService = patientDashboardService;
        this.adminDashboardStatusService = adminDashboardStatusService;
    }

    @GetMapping("/appointments/stats")
    public ResponseEntity<ApiResponse<PatientDashboardStatsDto>> getPatientStats(@CookieValue("jwt") String token) {
        UUID userId = JwtUtils.extractUserIdFromToken(token);
        long totalUpcoming = patientDashboardService.getTotalUpcomingAppointments(userId);
        long totalActiveDoctor = adminDashboardStatusService.getTotalDoctors();

        PatientDashboardStatsDto data = new PatientDashboardStatsDto(totalUpcoming,totalActiveDoctor);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Patient stats confirmed fetched successfully", data)
        );
    }

    @GetMapping("/appointments/upcoming")
    public ResponseEntity<ApiResponse<List<PatientAppointmentDto>>> getUpcomingAppointments(@CookieValue("jwt") String token) {
        UUID userId = JwtUtils.extractUserIdFromToken(token);
        List<PatientAppointmentDto> appointments = patientDashboardService.getUpcomingAppointments(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Upcoming confirmed appointments fetched successfully", appointments)
        );
    }

    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<List<PatientAppointmentDto>>> getAppointments(@CookieValue("jwt") String token) {
        UUID userId = JwtUtils.extractUserIdFromToken(token);
        List<PatientAppointmentDto> appointments = patientDashboardService.getAppointments(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "appointments fetched successfully", appointments)
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
