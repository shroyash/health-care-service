package com.example.healthcare.controller;

import com.example.healthcare.dto.response.ApiResponse;
import com.example.healthcare.dto.response.DoctorAppointmentDto;
import com.example.healthcare.dto.response.PatientAppointmentDto;
import com.example.healthcare.service.AppointmentHistoryService;
import com.example.healthcare.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments/history")
@RequiredArgsConstructor
public class AppointmentHistoryController {

    private final AppointmentHistoryService appointmentHistoryService;

    @GetMapping("/patient/my")
    public ResponseEntity<ApiResponse<List<PatientAppointmentDto>>> getPatientHistory(
            @CookieValue("jwt") String token
    ) {
        UUID patientId = JwtUtils.extractUserIdFromToken(token);
        List<PatientAppointmentDto> data = appointmentHistoryService.getPatientHistory(patientId);
        return ResponseEntity.ok(ApiResponse.<List<PatientAppointmentDto>>builder()
                .status(true)
                .message("Patient appointment history fetched successfully")
                .data(data)
                .build());
    }

    @GetMapping("/doctor/my")
    public ResponseEntity<ApiResponse<List<DoctorAppointmentDto>>> getDoctorHistory(
            @CookieValue("jwt") String token
    ) {
        UUID doctorId = JwtUtils.extractUserIdFromToken(token);
        List<DoctorAppointmentDto> data = appointmentHistoryService.getDoctorHistory(doctorId);
        return ResponseEntity.ok(ApiResponse.<List<DoctorAppointmentDto>>builder()
                .status(true)
                .message("Doctor appointment history fetched successfully")
                .data(data)
                .build());
    }
}