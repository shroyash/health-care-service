package com.example.healthcare.controller;

import com.example.healthcare.dto.*;
import com.example.healthcare.feign.AuthServiceClient;
import com.example.healthcare.service.AdminDashboardStatusService;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.service.PatientProfileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard/admin")
@RequiredArgsConstructor
@AllArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardStatusService adminDashboardStatusService;
    private final PatientProfileService patientProfileService;
    private final DoctorProfileService doctorProfileService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminDashboardStatsDto>> getDashboardStats() {
        AdminDashboardStatsDto stats = new AdminDashboardStatsDto(
                adminDashboardStatusService.getTotalAppointmentsToday(),
                adminDashboardStatusService.getTotalDoctors(),
                adminDashboardStatusService.getTotalPatients(),
                adminDashboardStatusService.getPendingDoctorApprovals()
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "Admin dashboard stats fetched successfully", stats));
    }

    @GetMapping("/recent-appointments")
    public ResponseEntity<ApiResponse<List<AppointmentFullDto>>> getRecentAppointments() {

            List<AppointmentFullDto> appointments = adminDashboardStatusService.getRecentAppointments();

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Recent appointments fetched successfully", appointments)
            );
        }
    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<List<DoctorProfileResponseDto>>> getAllDoctors() {

        List<DoctorProfileResponseDto> doctors = doctorProfileService.getAllDoctorProfiles();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "All Doctors fetched successfully", doctors)
        );
    }
    @GetMapping("/patients")
    public ResponseEntity<ApiResponse<List<PatientProfileDTO>>> getAllPatients() {

        List<PatientProfileDTO> patients = patientProfileService.getAllPatients();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "All patients fetched successfully", patients)
        );
    }

    }

