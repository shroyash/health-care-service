
package com.example.healthcare.appointmentRequest.controller;

import com.example.healthcare.appointmentRequest.dto.request.AppointmentRequestDto;
import com.example.healthcare.appointmentRequest.dto.response.AppointmentRequestResponseDto;
import com.example.healthcare.appointmentRequest.service.AppointmentRequestService;
import com.example.healthcare.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointment-requests/patient")
@RequiredArgsConstructor
public class PatientAppointmentRequestController {

    private final AppointmentRequestService appointmentRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createRequest(
            @Valid @RequestBody AppointmentRequestDto dto,
            @RequestHeader("X-User-Id") UUID patientId,
            @RequestHeader("X-Username") String username) {
        appointmentRequestService.createRequest(dto, patientId, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,
                        "Appointment request sent successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentRequestResponseDto>>> getMyRequests(
            @RequestHeader("X-User-Id") UUID patientId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Your appointment requests fetched",
                appointmentRequestService.getRequestsForPatient(patientId)));
    }
}