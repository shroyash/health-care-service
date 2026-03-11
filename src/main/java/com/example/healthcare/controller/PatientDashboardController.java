package com.example.healthcare.controller;

import com.example.healthcare.dto.response.*;
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
        long totalReportWritten = adminDashboardStatusService.getTotalReportWritten(userId);

        PatientDashboardStatsDto data = new PatientDashboardStatsDto(totalUpcoming,totalActiveDoctor,totalReportWritten);

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

    @GetMapping("/weekly-count")
    public ResponseEntity<ApiResponse<List<DailyAppointmentCountDto>>> getWeeklyAppointmentCount(
            @CookieValue("jwt") String token
    ) {
        UUID patientId = JwtUtils.extractUserIdFromToken(token);
        List<DailyAppointmentCountDto> counts = patientDashboardService.getPatientWeeklyAppointments(patientId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Weekly count fetched successfully", counts)
        );
    }

    @GetMapping("/status-count")
    public ResponseEntity<ApiResponse<List<AppointmentStatusCountDto>>> getStatusCount(
            @CookieValue("jwt") String token
    ) {
        UUID patientId = JwtUtils.extractUserIdFromToken(token);
        List<AppointmentStatusCountDto> counts = patientDashboardService.getStatusCount(patientId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Status count fetched successfully", counts)
        );
    }

}
