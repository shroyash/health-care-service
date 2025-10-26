package com.example.healthcare.controller;

import com.example.healthcare.dto.ApiResponse;
import com.example.healthcare.dto.AppointmentDoctorReceiveResponseDto;
import com.example.healthcare.dto.AppointmentPatientRequestResponseDto;
import com.example.healthcare.dto.AppointmentRequestDto;
import com.example.healthcare.service.AppointmentRequestService;
import com.example.healthcare.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRequestController {

    private final AppointmentRequestService service;

    public AppointmentRequestController(AppointmentRequestService service) {
        this.service = service;
    }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<String>> createRequest(
            @Valid @RequestBody AppointmentRequestDto requestDto,
            @CookieValue(name = "jwt", required = true) String token) {

        service.createRequest(requestDto, token);

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .status(true)
                .message("Appointment request created successfully")
                .data(null)
                .build());
    }

    @GetMapping("/doctor")
    public ResponseEntity<ApiResponse<List<AppointmentPatientRequestResponseDto>>> getRequestsForDoctor(
            @CookieValue(name = "jwt", required = true) String token) {

        long doctorId = JwtUtils.extractUserIdFromToken(token);
        List<AppointmentPatientRequestResponseDto> requests = service.getRequestsForDoctor(doctorId);

        return ResponseEntity.ok(ApiResponse.<List<AppointmentPatientRequestResponseDto>>builder()
                .status(true)
                .message("Fetched appointment requests for doctor")
                .data(requests)
                .build());
    }

    @GetMapping("/patient")
    public ResponseEntity<ApiResponse<List<AppointmentDoctorReceiveResponseDto>>> getRequestsForPatient(
            @CookieValue(name = "jwt", required = true) String token) {

        List<AppointmentDoctorReceiveResponseDto> requests = service.getRequestsForPatient(token);

        return ResponseEntity.ok(ApiResponse.<List<AppointmentDoctorReceiveResponseDto>>builder()
                .status(true)
                .message("Fetched your appointment requests")
                .data(requests)
                .build());
    }

    @PatchMapping("/update-status/{requestId}")
    public ResponseEntity<ApiResponse<AppointmentDoctorReceiveResponseDto>> updateStatus(
            @PathVariable Long requestId,
            @RequestParam String status,
            @CookieValue(name = "jwt", required = true) String token) {

        AppointmentDoctorReceiveResponseDto updatedRequest = service.updateStatus(requestId, status, token);

        return ResponseEntity.ok(ApiResponse.<AppointmentDoctorReceiveResponseDto>builder()
                .status(true)
                .message("Appointment status updated successfully")
                .data(updatedRequest)
                .build());
    }
}
