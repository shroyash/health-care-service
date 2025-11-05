package com.example.healthcare.controller;

import com.example.healthcare.dto.*;
import com.example.healthcare.service.AdminDashboardStatusService;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.service.PatientProfileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard/admin")
@RequiredArgsConstructor
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

    // Suspend a patient
    @PutMapping("/suspend/{patientId}")
    public ResponseEntity<ApiResponse<?>> suspendPatient(@PathVariable Long patientId) {
        var updatedPatient = patientProfileService.suspendPatient(patientId);
        ApiResponse<?> response = new ApiResponse<>(true, "Patient suspended successfully", updatedPatient);
        return ResponseEntity.ok(response);
    }

    // Restore a suspended patient
    @PutMapping("/restore/{patientId}")
    public ResponseEntity<ApiResponse<?>> restorePatient(@PathVariable Long patientId) {
        var updatedPatient = patientProfileService.restorePatient(patientId);
        ApiResponse<?> response = new ApiResponse<>(true, "Patient restored successfully", updatedPatient);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{doctorId}/suspend")
    public ResponseEntity<ApiResponse<DoctorProfileResponseDto>> suspendDoctor(@PathVariable Long doctorId) {
        DoctorProfileResponseDto doctor = doctorProfileService.suspendDoctor(doctorId);

        ApiResponse<DoctorProfileResponseDto> response = new ApiResponse<>(
                true,
                "Doctor suspended successfully",
                doctor
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{doctorId}/restore")
    public ResponseEntity<ApiResponse<DoctorProfileResponseDto>> restoreDoctor(@PathVariable Long doctorId) {
        DoctorProfileResponseDto doctor = doctorProfileService.restoreDoctor(doctorId);

        ApiResponse<DoctorProfileResponseDto> response = new ApiResponse<>(
                true,
                "Doctor restored successfully",
                doctor
        );

        return ResponseEntity.ok(response);
    }

}

